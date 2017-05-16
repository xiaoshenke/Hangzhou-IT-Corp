package wuxian.me.spidersdk;

import com.sun.istack.internal.NotNull;
import okhttp3.*;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.anti.FailHelper;
import wuxian.me.spidersdk.anti.HeartbeatManager;
import wuxian.me.spidersdk.anti.IPProxyTool;
import wuxian.me.spidersdk.control.*;
import wuxian.me.spidersdk.distribute.MethodCheckException;
import wuxian.me.spidersdk.distribute.RedisServerConnectionException;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.util.OkhttpProvider;
import wuxian.me.spidersdk.util.ProcessSignalManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wuxian on 9/4/2017.
 * <p>
 * 统筹管理所有job,
 * 1 管理WorkThread,JobQueue,JobMonitor
 * 2 负责处理job的成功失败 --> 失败是否重试
 * 3 负责处理ip被屏蔽 --> 是则停止现有job,切换ip,打监控日志,重启workThread等等
 * <p>
 */
public class JobManager implements HeartbeatManager.IHeartBeat {

    private ProcessSignalManager signalManager = new ProcessSignalManager();

    private JobMonitor monitor = new JobMonitor();
    private IQueue queue;//= new JobQueue(monitor);

    private WorkThread workThread = new WorkThread(this);

    private HeartbeatManager heartbeatManager = new HeartbeatManager();
    private IPProxyTool ipProxyTool = new IPProxyTool();
    private AtomicBoolean isSwitchingIP = new AtomicBoolean(false);

    //记录单次(单个ip)的spiderList --> OkHttpClient.enqueue的spider 还有一部分delayJob是没办法拿到的...
    private List<BaseSpider> todoSpiderList = Collections.synchronizedList(new ArrayList<BaseSpider>());
    private Dispatcher dispatcher = OkhttpProvider.getClient().dispatcher();

    private FailHelper failHelper = new FailHelper();
    private AtomicInteger successJobNum = new AtomicInteger(0);
    private long workThreadStartTime;//WorkThread线程开启的时间

    private boolean started = false;
    private static JobManager instance;

    public static JobManager getInstance() throws RedisServerConnectionException, MethodCheckException {
        if (instance == null) {
            instance = new JobManager();
        }
        return instance;
    }


    private JobManager() throws RedisServerConnectionException, MethodCheckException {
        signalManager.registerOnSystemKill(new ProcessSignalManager.OnSystemKill() {
            public void onSystemKilled() {
                onPause();
            }
        });
        signalManager.init();

        if (JobManagerConfig.useRedisJobQueue) {
            queue = new RedisJobQueue();
        } else {
            queue = new JobQueue(monitor);
        }

        //Todo
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                onUncaughtException(t, e);
            }
        });
        onResume();
    }

    //Todo:检查各个部件是否工作良好
    public void onUncaughtException(Thread t, Throwable e) {

    }

    //Enable resume Non-Finished job
    //Todo
    public void onResume() {
        ;
    }

    //Enable resume Non-Finished job
    //Todo
    public void onPause() {
        ;
    }

    //单独调用
    //MUST BE CALLED MANUALLY!
    public void start() {
        if (!started) {
            started = true;
            heartbeatManager.addHeartBeat(this);
            workThreadStartTime = System.currentTimeMillis();
            LogManager.info("WorkThread started");
            workThread.start();
        }
    }

    public boolean putJob(@NotNull IJob job) {
        return queue.putJob(job, IJob.STATE_INIT);
    }

    public IJob getJob() {
        IJob job = queue.getJob();
        //LogManager.info("getJob: " + ((BaseSpider) (job.getRealRunnable())).name());
        return job;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void register(@NotNull BaseSpider spider) {
        //LogManager.info("JobManager.register " + spider.name());
        todoSpiderList.add(spider);
    }

    public void success(Runnable runnable) {
        successJobNum.getAndIncrement();
        failHelper.removeFail(runnable);

        IJob job = monitor.getJob(runnable);
        if (job == null) { //SHOULD NEVER HAPPEN!
            return;
        }
        LogManager.info("Job Success: " + ((BaseSpider) (job.getRealRunnable())).name());
        todoSpiderList.remove(job);
        monitor.putJob(job, IJob.STATE_SUCCESS);

    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail) {
        fail(runnable, fail, true);
    }

    //注意实现线程安全
    public void fail(@NotNull Runnable runnable, @NotNull Fail fail, boolean retry) {

        if (isSwitchingIP.get()) {
            IJob job = monitor.getJob(runnable);
            if (job != null) {
                //FailHelper不用做什么 因为已知被block
                //TodoSpiderList不做remove 这些job会被重新丢入到jobQueue
                //不用调job.fail来增加失败次数 因为没有意义
                monitor.putJob(job, IJob.STATE_FAIL);
            }
        } else {
            failHelper.addFail(runnable, fail);

            IJob job = monitor.getJob(runnable);
            if (job != null) {
                todoSpiderList.remove(job);  //这里先remove掉 因为在OkHttpClient.enqueue的时候会被重新加入

                job.fail(fail);
                monitor.putJob(job, IJob.STATE_FAIL);

                if (retry && JobManagerConfig.enableRetrySpider) {
                    if (job.getFailTimes() >= JobManagerConfig.singleJobMaxFailTimes) { //重试处理
                        LogManager.error("Job: " + job.toString() + " fail " + job.getFailTimes() + "times, abandon it");
                    } else {
                        LogManager.info("Retry Job: " + job.toString());
                        IJob next = JobProvider.getNextJob(job);
                        queue.putJob(next, IJob.STATE_RETRY);
                    }
                }
            }
            if (failHelper.isBlock()) {
                LogManager.error("WE ARE BLOCKED!");
                LogManager.error("Until now, We have success " + successJobNum.get() +
                        " jobs, We have run " + (System.currentTimeMillis() - workThreadStartTime) / 1000 + " seconds");
                heartbeatManager.stopHeartBeat();
                dealBlock();
            }
        }
    }

    private void dealBlock() {
        if (JobManagerConfig.enableSwitchProxy) {
            LogManager.info("We begin to switch IP...");
            doSwitchIp();
        } else {        //被block后就停止了 --> 主要用于测试
            isSwitchingIP.set(true);

            LogManager.info("We will not switch IP ");
            LogManager.info("We have total " + monitor.getWholeJobNum() + " jobs, we have "
                    + queue.getJobNum() + " jobs in JobQueue, we have "
                    + todoSpiderList.size() + "jobs in todoSpiderList");
            LogManager.info("We have " + dispatcher.runningCallsCount() +
                    " request running and " + dispatcher.queuedCallsCount() + " request queue in OkHttpClient");

            dispatcher.cancelAll();
            workThread.pauseWhenSwitchIP();
            monitor.printAllJobStatus();
        }
    }

    private void doSwitchIp() {
        isSwitchingIP.set(true);
        workThread.pauseWhenSwitchIP();
        LogManager.info("WorkThread paused");

        dispatcher.cancelAll();

        LogManager.info("We will not switch IP ");
        LogManager.info("We have total " + monitor.getWholeJobNum() + " jobs, we have "
                + queue.getJobNum() + " jobs in JobQueue, we have "
                + todoSpiderList.size() + " jobs in todoSpiderList");
        LogManager.info("We have " + dispatcher.runningCallsCount() +
                " request running and " + dispatcher.queuedCallsCount() +
                " request queue in OkHttpClient");

        monitor.printAllJobStatus();  //监控用

        while (true) {  //每个ip尝试三次 直到成功或没有proxy
            IPProxyTool.Proxy proxy = ipProxyTool.switchNextProxy();
            if (proxy == null) {

                if (JobManagerConfig.enableRuntimeInputProxy) {
                    ipProxyTool.openShellAndEnsureProxyInputed();

                    proxy = ipProxyTool.switchNextProxy();
                } else {
                    throw new RuntimeException("Proxy is not available!");
                }

            }

            LogManager.info("We try to switch to Ip: " + proxy.ip + " Port: " + proxy.port);
            int ensure = 0;
            boolean success = false;
            while (!(success = ipSwitched(proxy)) && ensure < JobManagerConfig.everyProxyTryTime) {  //每个IP尝试三次
                ensure++;
            }
            if (success) {
                LogManager.info("Switch Proxy success");
                break;
            }
        }

        dispatcher.cancelAll();
        for (BaseSpider spider : todoSpiderList) {
            IJob job = monitor.getJob(spider);
            if (job != null) {
                queue.putJob(job, IJob.STATE_INIT);
            }
        }
        todoSpiderList.clear();

        failHelper.reInit();
        workThreadStartTime = System.currentTimeMillis();
        LogManager.info("WorkThread resumed");
        workThread.resumeNow();
        isSwitchingIP.set(false);
    }

    public IPProxyTool.Proxy switchProxy() {
        return ipProxyTool.switchNextProxy();
    }

    private boolean ensureIpSwitched(final IPProxyTool.Proxy proxy)
            throws InterruptedException, ExecutionException {
        return ipProxyTool.ensureIpSwitched(proxy);
    }

    //外部统一调这个...
    public boolean ipSwitched(final IPProxyTool.Proxy proxy) {
        return this.ipSwitched(proxy, false);
    }

    public boolean ipSwitched(final IPProxyTool.Proxy proxy, boolean beginHeartBeat) {
        try {
            boolean ret = ensureIpSwitched(proxy);
            if (ret && beginHeartBeat) {
                heartbeatManager.beginHeartBeat(proxy);
            }
            return ret;
        } catch (InterruptedException e1) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }


    public void onHeartBeatBegin() {
        //暂时不用做什么
    }

    public void onHeartBeat(int time) {
        LogManager.info("onHeartBeat " + time);
    }


    public void onHeartBeatFail() {
        LogManager.info("onHeartBeatFail");
        dealBlock();  //这里和block差不多等价
    }

    //JobManager主动调用HeartbeatManager.stopxxx
    public void onHeartBeatInterrupt() {
        LogManager.info("onHeartBeatInterrupt");
    }
}
