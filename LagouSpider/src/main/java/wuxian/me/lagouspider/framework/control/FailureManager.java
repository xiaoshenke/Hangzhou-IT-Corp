package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.framework.IPProxyTool;
import wuxian.me.lagouspider.framework.OkhttpProvider;

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

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 9/4/2017.
 * 根据失败的情况判断是否ip被屏蔽了
 *
 * 只暴露给 @JobMonitor
 */
public class FailureManager {

    private FutureTask<String> switchIPFuture;

    private long startTime = System.currentTimeMillis();
    private FailureManager() {
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
        switchIPFuture = new FutureTask<String>(callable);
    }

    private static FailureManager instance;

    public static FailureManager getInstance() {
        if (instance == null) {
            instance = new FailureManager();
        }
        return instance;
    }

    AtomicLong current404Time = new AtomicLong(0);
    AtomicInteger successNum = new AtomicInteger(0);
    AtomicInteger failNum = new AtomicInteger(0);

    public void success(@NotNull IJob job) {
        logger().info("Success: " + ((BaseSpider) job.getRealRunnable()).name());
        successNum.getAndIncrement();

        if (todoSpiderList.contains(job)) {
            todoSpiderList.remove(job);
        }
    }

    private AtomicInteger fail404Num = new AtomicInteger(0);
    private AtomicLong last404Time = new AtomicLong(0);

    private AtomicInteger failNetErrNum = new AtomicInteger(0);
    private AtomicLong lastNetErrTime = new AtomicLong(0);
    private AtomicLong currentNetErrTime = new AtomicLong(0);

    private AtomicInteger failMaybeBlockNum = new AtomicInteger(0);
    private AtomicLong lastMaybeBlockTime = new AtomicLong(0);
    private AtomicLong currentMaybeBlockTime = new AtomicLong(0);

    //记录单次(单个ip)的spiderList
    private List<BaseSpider> todoSpiderList = Collections.synchronizedList(new ArrayList<BaseSpider>());

    public void register(@NotNull BaseSpider spider) {
        todoSpiderList.add(spider);
    }


    //Todo: 不同的网站的判定被判定被屏蔽的策略应该是不一样的
    public void fail(@NotNull IJob job, @NotNull Fail fail) {
        if (isSwitchingIP.get()) {  //失败的延迟任务还是会被dispatch到FailMonitor,这里直接丢弃
            return;
        }

        if (todoSpiderList.contains(job)) {
            todoSpiderList.remove(job);
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
            currentNetErrTime.set(lastNetErrTime.get());

        } else if (fail.isMaybeBlock()) {
            failMaybeBlockNum.incrementAndGet();
            lastMaybeBlockTime.set(currentMaybeBlockTime.get());
            currentMaybeBlockTime.set(lastMaybeBlockTime.get());

        }

        if (isBlocked(fail)) {
            logger().info("WE ARE BLOCKED! Until Now we have success " + successNum.get() +
                    " jobs, we have run " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
            if (Config.ENABLE_SWITCH_IPPROXY) {
                doSwitchIp();
            } else {        //被block后就停止了
                isSwitchingIP.set(true);
                OkhttpProvider.getClient().dispatcher().cancelAll();
                WorkThread.getInstance().pauseWhenSwitchIP();
            }
        }
    }

    private boolean isBlocked(@NotNull Fail fail) {
        if (fail.isBlock()) {
            return true;
        }
        if (fail404Num.get() >= 2) {
            return true;
        }

        if (failMaybeBlockNum.get() >= 3 || currentMaybeBlockTime.get() - lastMaybeBlockTime.get() < 300) {
            return true;
        }

        if (failNetErrNum.get() > 5 || currentNetErrTime.get() - lastNetErrTime.get() < 500) {
            return true;
        }
        return false;
    }

    private AtomicBoolean isSwitchingIP = new AtomicBoolean(false);

    private void doSwitchIp() {
        isSwitchingIP.set(true);
        OkhttpProvider.getClient().dispatcher().cancelAll();
        WorkThread.getInstance().pauseWhenSwitchIP();
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
        OkhttpProvider.getClient().dispatcher().cancelAll();

        for (BaseSpider spider : todoSpiderList) {
            IJob job = JobMonitor.getInstance().getJob(spider);
            job.setCurrentState(IJob.STATE_INIT);

            JobQueue.getInstance().putJob(job, IJob.STATE_INIT); //会同步跟新JobMonitor的状态
        }

        todoSpiderList.clear();
        WorkThread.getInstance().resumeNow();
        isSwitchingIP.set(false);
    }

    private void reset() {
        fail404Num.set(0);
        failNetErrNum.set(0);
    }

    private boolean ensureIpSwitched(final IPProxyTool.Proxy proxy) {
        new Thread(switchIPFuture).start();
        try {
            return switchIPFuture.get() == null ?
                    false : switchIPFuture.get().contains(proxy.ip);
        } catch (InterruptedException e1) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
