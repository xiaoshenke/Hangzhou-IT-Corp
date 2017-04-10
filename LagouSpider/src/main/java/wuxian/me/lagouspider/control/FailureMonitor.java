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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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

    private AtomicLong current404Time = new AtomicLong(0);
    AtomicInteger successNum = new AtomicInteger(0);
    AtomicInteger failNum = new AtomicInteger(0);

    public void success(@NotNull IJob job) {
        logger().info("Success: " + ((BaseLagouSpider) job.getRealRunnable()).simpleName());
        successNum.getAndIncrement();
    }

    private AtomicInteger fail404Num = new AtomicInteger(0);

    private AtomicLong last404Time = new AtomicLong(0);

    private AtomicInteger failNetErrNum = new AtomicInteger(0);
    private AtomicLong lastNetErrTime = new AtomicLong(0);
    private AtomicLong currentNetErrTime = new AtomicLong(0);


    public void fail(@NotNull IJob job, @NotNull Fail fail) {
        if (isSwitchingIP.get()) {  //失败的延迟任务还是会被dispatch到FailMonitor,这里直接丢弃
            return;
        }
        if (Config.IS_TEST && failNum.get() >= 10) {  //为了测试 先这么搞
            return;
        }
        failNum.getAndIncrement();

        logger().debug(job.toString() + " Fail reason: " + fail.toString());

        if (fail.is404()) {
            fail404Num.incrementAndGet();

            last404Time.set(current404Time.get());
            current404Time.set(fail.millis);
        } else if (fail.isNetworkErr()) {
            failNetErrNum.incrementAndGet();

            lastNetErrTime.set(currentNetErrTime.get());
            currentNetErrTime.set(last404Time.get());
        }

        if (isBlocked(fail)) {
            logger().error("WE ARE BLOCKED! We have success " + successNum.get() + " jobs");
            if (Config.ENABLE_SWITCH_IPPROXY) {
                doSwitchIp();
            }

            if (Config.IS_TEST) {         //被block后就停止了
                isSwitchingIP.set(true);
                WorkThread.getInstance().pauseWhenSwitchIP(); //Fixme:没什么卵用 请求都被放在OkHttpClient的pool里 任务还是一直在执行
            }
        }

    }

    private boolean isBlocked(@NotNull Fail fail) {
        if (fail.isBlock()) {
            return true;
        }
        if (fail404Num.get() > 10 || current404Time.get() - last404Time.get() < 500) { //已经有了10个404且相隔不久
            return true;
        }

        if (failNetErrNum.get() > 10 || currentNetErrTime.get() - lastNetErrTime.get() < 500) {
            return true;
        }
        return false;
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
        fail404Num.set(0);
        failNetErrNum.set(0);
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
}
