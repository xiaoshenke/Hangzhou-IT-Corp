package wuxian.me.lagouspider.control;

import com.sun.istack.internal.NotNull;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.core.BaseLagouSpider;
import wuxian.me.lagouspider.job.IJob;
import wuxian.me.lagouspider.util.IPProxyTool;
import wuxian.me.lagouspider.util.OkhttpProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static wuxian.me.lagouspider.Config.FAIL_FREQUENCY_NETWORK_ERR;
import static wuxian.me.lagouspider.Config.FAIL_FREQUENCY_SERVER_ERR;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 9/4/2017.
 * 根据失败的情况判断是否ip被屏蔽了:
 * 目前只根据fail的频率 不去管job自身的状态 --> 短期大量的失败认为被屏蔽了 或者网络条件很差
 */
public class FailureMonitor {

    private FutureTask<String> confirmSwitchIPFuture;

    private FailureMonitor() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://www.ip138.com/ip2city.asp").newBuilder();
        Headers.Builder builder = new Headers.Builder();
        builder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        final Request request = new Request.Builder()
                .headers(builder.build())
                .url(urlBuilder.build().toString())
                .build();

        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                try {
                    Response response = OkhttpProvider.getClient().newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    return null;
                }
            }
        };
        confirmSwitchIPFuture = new FutureTask<String>(callable);
    }

    private static FailureMonitor instance;

    public static FailureMonitor getInstance() {
        if (instance == null) {
            instance = new FailureMonitor();
        }
        return instance;
    }

    private AtomicLong firstFailTime = new AtomicLong(-1);
    private AtomicLong currentFailTime;
    private AtomicInteger networkErrCount = new AtomicInteger(0);
    List<Fail> failList = Collections.synchronizedList(new ArrayList<Fail>());

    AtomicInteger successNum = new AtomicInteger(0);
    AtomicInteger failNum = new AtomicInteger(0);

    public void success(@NotNull IJob job) {
        logger().info("Success: " + ((BaseLagouSpider) job.getRealRunnable()).simpleName());
        successNum.getAndIncrement();
    }

    public void fail(@NotNull IJob job, @NotNull Fail fail) {
        if (isSwitchingIP.get()) {  //失败的延迟任务还是会被dispatch到FailMonitor,这里直接丢弃
            return;
        }
        if (Config.IS_TEST && failNum.get() >= 10) {  //为了测试 先这么搞
            return;
        }
        failNum.getAndIncrement();

        logger().error(job.toString() + " Fail reason: " + fail.toString());
        //Todo: 404错误码

        if (firstFailTime.get() == -1) {
            firstFailTime = new AtomicLong(fail.millis);
            logger().error("FirstFailTime: " + firstFailTime.get());
        }

        if (fail.isBlock()) {
            logger().error("WE ARE BLOCKED! We have success " + successNum.get() + " jobs,while fail " + failNum.get() + " jobs, we have already run " + (System.currentTimeMillis() - firstFailTime.get()) / 1000 + " second");
            if (Config.ENABLE_SWITCH_IPPROXY) {
                doSwitchIp();
            }

            if (Config.IS_TEST) {         //被block后就停止了
                isSwitchingIP.set(true);
                WorkThread.getInstance().pauseWhenSwitchIP();
            }
        }

        currentFailTime = new AtomicLong(fail.millis);

        /*  //暂时先注释掉
        if (fail.isNetworkErr()) {
            networkErrCount.set(networkErrCount.get() + 1);
        } else {
            failList.add(fail);
        }

        if (shouldSwitchProxy()) {
            doSwitchIp();
        }
        */
    }

    private AtomicBoolean isSwitchingIP = new AtomicBoolean(false);
    private void doSwitchIp() {
        isSwitchingIP.set(true);
        WorkThread.getInstance().pauseWhenSwitchIP();  //只需停止workThread,尽管有的延迟任务还是会继续,那不管了
        reset();
        int times = 0;
        while (true) {
            if (times > 10) {
                throw new RuntimeException("No Proxy available!");
            }
            IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
            if (ensureIpSwitched(proxy)) {
                break;
            }
            times++;
        }
        WorkThread.getInstance().resumeNow();
        isSwitchingIP.set(false);
    }

    private void reset() {
        networkErrCount.set(0);
        firstFailTime.set(-1);
        failList.clear();
    }

    private boolean ensureIpSwitched(final IPProxyTool.Proxy proxy) {
        new Thread(confirmSwitchIPFuture).start();
        try {
            return confirmSwitchIPFuture.get() == null ?
                    false : confirmSwitchIPFuture.get().contains(proxy.ip);
        } catch (InterruptedException e1) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }

    private boolean shouldSwitchProxy() {
        if (currentFailTime.get() - firstFailTime.get() < 1000 * 60) {  //小于1分钟跳过
            return false;
        }

        int minute = (int) (currentFailTime.get() - firstFailTime.get()) / (1000 * 60);
        if (networkErrCount.get() / minute >= FAIL_FREQUENCY_NETWORK_ERR) {
            return true;
        } else if (failList.size() / minute >= FAIL_FREQUENCY_SERVER_ERR) {
            return true;
        }
        return false;
    }
}
