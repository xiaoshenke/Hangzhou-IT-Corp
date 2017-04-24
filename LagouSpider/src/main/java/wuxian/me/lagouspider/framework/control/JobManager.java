package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import okhttp3.*;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.framework.HeartbeatManager;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.framework.IPProxyTool;
import wuxian.me.lagouspider.framework.OkhttpProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static wuxian.me.lagouspider.Config.ProxyControl.ENABLE_SWITCH_IPPROXY;
import static wuxian.me.lagouspider.Config.RetryControl.ENABLE_RETRY_SPIDER;
import static wuxian.me.lagouspider.Config.RetryControl.SINGLEJOB_MAX_FAIL_TIME;
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
public class JobManager implements HeartbeatManager.IHeartBeat {

    private JobMonitor monitor = new JobMonitor();
    private JobQueue queue = new JobQueue(monitor);
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

    public static JobManager getInstance() {
        if (instance == null) {
            instance = new JobManager();
        }
        return instance;
    }

    private JobManager() {

    }

    //单独调用
    //MUST BE CALLED MANUALLY!
    public void start() {
        if (!started) {
            started = true;
            heartbeatManager.addHeartBeat(this);
            workThreadStartTime = System.currentTimeMillis();
            logger().info("WorkThread started");
            workThread.start();
        }
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
        logger().info("JobManager.register " + spider.name());
        todoSpiderList.add(spider);
    }

    public void success(Runnable runnable) {
        successJobNum.getAndIncrement();
        failHelper.removeFail(runnable);

        IJob job = monitor.getJob(runnable);
        if (job == null) { //SHOULD NEVER HAPPEN!
            return;
        }
        logger().info("Job Success: " + ((BaseSpider) (job.getRealRunnable())).name());
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

                if (retry && ENABLE_RETRY_SPIDER) {
                    if (job.getFailTimes() >= SINGLEJOB_MAX_FAIL_TIME) { //重试处理
                        logger().error("Job: " + job.toString() + " fail " + job.getFailTimes() + "times, abandon it");
                    } else {
                        logger().info("Retry Job: " + job.toString());
                        IJob next = JobProvider.getNextJob(job);
                        queue.putJob(next, IJob.STATE_RETRY);
                    }
                }
            }
            if (failHelper.isBlock()) {
                logger().error("WE ARE BLOCKED!");
                logger().error("Until now, We have success " + successJobNum.get() +
                        " jobs, We have run " + (System.currentTimeMillis() - workThreadStartTime) / 1000 + "seconds");
                heartbeatManager.stopHeartBeat();
                dealBlock();
            }
        }
    }

    private void dealBlock() {
        if (ENABLE_SWITCH_IPPROXY) {
            logger().info("We begin to switch IP...");
            doSwitchIp();
        } else {        //被block后就停止了 --> 主要用于测试
            isSwitchingIP.set(true);

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

    private void doSwitchIp() {
        isSwitchingIP.set(true);
        workThread.pauseWhenSwitchIP();
        logger().info("WorkThread paused");

        dispatcher.cancelAll();

        logger().info("We will not switch IP ");
        logger().info("We have total " + monitor.getWholeJobNum() + " jobs, we have "
                + queue.getJobNum() + " jobs in JobQueue, we have "
                + todoSpiderList.size() + " jobs in todoSpiderList");
        logger().info("We have " + dispatcher.runningCallsCount() +
                " request running and " + dispatcher.queuedCallsCount() +
                " request queue in OkHttpClient");

        monitor.printAllJobStatus();  //监控用

        while (true) {  //每个ip尝试三次 直到成功或没有proxy
            IPProxyTool.Proxy proxy = ipProxyTool.switchNextProxy();
            if (proxy == null) {
                throw new RuntimeException("Proxy is not available!");
            }

            logger().info("We try to switch to Ip: " + proxy.ip + " Port: " + proxy.port);
            int ensure = 0;
            boolean success = false;
            while (!(success = ipSwitched(proxy)) && ensure < 3) {  //每个IP尝试三次
                ensure++;
            }
            if (success) {
                logger().info("Switch Proxy success");
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
        logger().info("WorkThread resumed");
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
        logger().info("onHeartBeat " + time);
    }


    public void onHeartBeatFail() {
        logger().info("onHeartBeatFail");
        dealBlock();  //这里和block差不多等价
    }

    //JobManager主动调用HeartbeatManager.stopxxx
    public void onHeartBeatInterrupt() {
        logger().info("onHeartBeatInterrupt");
    }
}
