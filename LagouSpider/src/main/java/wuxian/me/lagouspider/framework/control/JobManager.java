package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import okhttp3.*;
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
 * <p>
 * 统筹管理所有job,
 * 1 管理WorkThread,JobQueue,JobMonitor
 * 2 负责处理job的成功失败 --> 失败是否重试
 * 3 负责处理ip被屏蔽 --> 是则停止现有job,切换ip,打监控日志,重启workThread等等
 * <p>
 * <p>
 * 日志级别规定:
 * 1 监控整个项目运行的info级别 比如切换ip,job状态切换:开始运行,成功,失败,重试等
 * 2 Job出错的error级
 * 3 其他debug级别 比如parsing什么的
 */
public class JobManager {

    //Todo: 监控优化 不要用这些AtomicLong值了
    AtomicLong current404Time = new AtomicLong(0);
    AtomicInteger successNum = new AtomicInteger(0);
    AtomicInteger failNum = new AtomicInteger(0);

    private AtomicInteger fail404Num = new AtomicInteger(0);
    private AtomicLong last404Time = new AtomicLong(0);

    private AtomicInteger failNetErrNum = new AtomicInteger(0);
    private AtomicLong lastNetErrTime = new AtomicLong(0);
    private AtomicLong currentNetErrTime = new AtomicLong(0);

    private AtomicInteger failMaybeBlockNum = new AtomicInteger(0);
    private AtomicLong lastMaybeBlockTime = new AtomicLong(0);
    private AtomicLong currentMaybeBlockTime = new AtomicLong(0);

    //记录单次(单个ip)的spiderList --> OkHttpClient.enqueue的spider 还有一部分delayJob是没办法拿到的...
    private List<BaseSpider> todoSpiderList = Collections.synchronizedList(new ArrayList<BaseSpider>());

    private JobMonitor monitor = new JobMonitor();
    private JobQueue queue = new JobQueue(monitor);
    private WorkThread workThread = new WorkThread();

    private AtomicBoolean isSwitchingIP = new AtomicBoolean(false);
    private FutureTask<String> switchIPFuture;

    private long startTime = System.currentTimeMillis();

    private static JobManager instance;

    public static JobManager getInstance() {
        if (instance == null) {
            instance = new JobManager();
        }
        return instance;
    }

    private JobManager() {
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

    //单独调用
    public void start() {
        workThread.start();
    }

    public boolean putJob(@NotNull IJob job) {
        return queue.putJob(job);
    }

    public IJob getJob() {
        return queue.getJob();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void register(@NotNull BaseSpider spider) {
        todoSpiderList.add(spider);
    }

    public void success(Runnable runnable) {
        IJob job = monitor.getJob(runnable);
        if (job != null) {
            monitor.putJob(job, IJob.STATE_SUCCESS);

            success(job);
        }
    }

    private void success(@NotNull IJob job) {
        logger().info("Job Success: " + ((BaseSpider) job).name());
        successNum.getAndIncrement();

        if (todoSpiderList.contains(job)) {
            todoSpiderList.remove(job);
        }
    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail) {
        fail(runnable, fail, true);
    }

    //Fail并且判断是否应该进行job重试 若是则放入重试队列
    public void fail(@NotNull Runnable runnable, @NotNull Fail fail, boolean retry) {
        IJob job = monitor.getJob(runnable);
        if (job != null) {
            job.fail(fail);
            monitor.putJob(job, IJob.STATE_FAIL);  //更新JobQueue里的job状态

            if (retry && Config.ENABLE_RETRY_SPIDER) { //处理重试
                if (job.getFailTimes() >= Config.SINGLEJOB_MAX_FAIL_TIME) { //是否进行重试
                    logger().error("Job: " + job.toString() + " fail " + job.getFailTimes() + "times, abandon it");
                } else {
                    logger().info("Retry Job: " + job.toString());

                    IJob next = JobProvider.getNextJob(job);  //重新制定爬虫策略 放入jobQueue

                    next.setCurrentState(IJob.STATE_RETRY);
                    queue.putJob(next, IJob.STATE_RETRY);
                }
            }

            fail(job, fail);
        }
    }

    private void fail(@NotNull IJob job, @NotNull Fail fail) {
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
            logger().error("WE ARE BLOCKED! Until now We have success " + successNum.get() +
                    " jobs, we have run " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");

            if (Config.ENABLE_SWITCH_IPPROXY) {
                logger().info("We begin to switch IP...");
                doSwitchIp();
            } else {        //被block后就停止了
                isSwitchingIP.set(true);
                Dispatcher dispatcher = OkhttpProvider.getClient().dispatcher();

                logger().info("We will not switch IP ");

                logger().info("We have total " + monitor.getWholeJobNum() + " jobs, we have "
                        + queue.getJobNum() + " jobs in JobQueue, we have "
                        + todoSpiderList.size() + "jobs in todoSpiderList");

                logger().info("We have " + dispatcher.runningCallsCount() +
                        " request running and " + dispatcher.queuedCallsCount() + " request queue in OkHttpClient");

                dispatcher.cancelAll();
                workThread.pauseWhenSwitchIP();
                monitor.printAllJobStatus();
            }
        }
    }

    private boolean isBlocked(@NotNull Fail fail) {
        if (fail.isBlock()) {
            return true;
        }
        if (fail404Num.get() >= 1) {
            return true;
        }

        if (failMaybeBlockNum.get() >= 1 || currentMaybeBlockTime.get() - lastMaybeBlockTime.get() < 300) {
            return true;
        }

        if (failNetErrNum.get() >= 1 || currentNetErrTime.get() - lastNetErrTime.get() < 500) {
            return true;
        }
        return false;
    }

    private void doSwitchIp() {
        isSwitchingIP.set(true);
        OkhttpProvider.getClient().dispatcher().cancelAll();
        workThread.pauseWhenSwitchIP();
        monitor.printAllJobStatus();
        reset();
        int times = 0;
        while (true) {  //每个ip尝试三次 直到成功或没有proxy
            IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
            if (proxy == null) {
                throw new RuntimeException("Proxy is not available!");
            }

            logger().info("We try to switch to Ip: " + proxy.ip + " Port: " + proxy.port);
            int ensure = 0;
            boolean success = false;
            while (!(success = ensureIpSwitched(proxy)) && ensure < 3) {  //每个IP尝试三次
                ensure++;
            }
            if (success) {
                logger().info("Switch Proxy success");
                break;
            }
            times++;
        }

        OkhttpProvider.getClient().dispatcher().cancelAll();
        for (BaseSpider spider : todoSpiderList) {
            IJob job = monitor.getJob(spider);
            job.setCurrentState(IJob.STATE_INIT);

            queue.putJob(job, IJob.STATE_INIT); //会同步跟新JobMonitor的状态
        }

        todoSpiderList.clear();
        workThread.resumeNow();
        isSwitchingIP.set(false);
    }

    private void reset() {
        fail404Num.set(0);
        failNetErrNum.set(0);
        failMaybeBlockNum.set(0);
    }

    private boolean ensureIpSwitched(final IPProxyTool.Proxy proxy) {
        new Thread(switchIPFuture).start();
        try {
            return switchIPFuture.get() == null ? false : switchIPFuture.get().contains(proxy.ip);
        } catch (InterruptedException e1) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
