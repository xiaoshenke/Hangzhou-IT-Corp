package wuxian.me.spidersdk.manager;

import com.sun.istack.internal.NotNull;
import okhttp3.Dispatcher;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.IJobManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.anti.BlockHelper;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.anti.HeartbeatManager;
import wuxian.me.spidersdk.anti.IPProxyTool;
import wuxian.me.spidersdk.control.IQueue;
import wuxian.me.spidersdk.control.RedisJobQueue;
import wuxian.me.spidersdk.control.WorkThread;
import wuxian.me.spidersdk.distribute.*;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.util.FileUtil;
import wuxian.me.spidersdk.util.JobManagerMonitor;
import wuxian.me.spidersdk.util.OkhttpProvider;
import wuxian.me.spidersdk.util.ShellUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarFile;

/**
 * Created by wuxian on 18/5/2017.
 * <p>
 * 分布式下(且没有身份的)的job manager
 * Todo:关键节点打点
 */
public class DistributeJobManager implements IJobManager, HeartbeatManager.IHeartBeat {

    private IQueue queue;
    private ClassHelper.CheckFilter checkFilter;
    private WorkThread workThread = new WorkThread(this);
    private boolean started = false;

    private JobManagerMonitor monitor = new JobManagerMonitor();
    private HeartbeatManager heartbeatManager = new HeartbeatManager();
    private Dispatcher dispatcher = OkhttpProvider.getClient().dispatcher();

    private List<BaseSpider> successSpiderList = Collections.synchronizedList(new ArrayList<BaseSpider>());

    private List<BaseSpider> failureSpiderList = Collections.synchronizedList(new ArrayList<BaseSpider>());
    private BlockHelper blockHelper = new BlockHelper();

    private AtomicBoolean isSwitchingIP = new AtomicBoolean(false);
    private IPProxyTool ipProxyTool = new IPProxyTool();

    //位于okHttpClient的缓存池中
    private List<BaseSpider> dispatchedSpiderList = Collections.synchronizedList(new ArrayList<BaseSpider>());

    public void setSpiderChecker(@NotNull ClassHelper.CheckFilter filter) {
        this.checkFilter = filter;
    }

    public DistributeJobManager() {
        init();
    }

    private void init() {

        ShellUtil.init();

        checkAndColloectSubSpiders();

        queue = new RedisJobQueue();

    }

    /**
     * 收集本jar包下的所有合法的@BaseSpider子类
     */
    private void checkAndColloectSubSpiders() {
        Set<Class<?>> classSet = null;
        if (JobManagerConfig.jarMode) {
            if (FileUtil.currentFile != null) {
                try {
                    JarFile jar = new JarFile(FileUtil.currentFile);
                    classSet = ClassHelper.getJarFileClasses(jar, null, checkFilter);

                } catch (IOException e) {

                }
            } else {
                throw new MethodCheckException("FileUtil.currentFile is not set!");
            }
        } else {
            try {           //Fixme:library模式下 这段代码不起作用,应该改成业务的包名
                classSet = ClassHelper.getClasses("wuxian.me.spidersdk");
            } catch (IOException e) {
                classSet = null;
            }
        }
        if (classSet == null) {
            return;
        }
        for (Class<?> clazz : classSet) {
            SpiderMethodTuple tuple = SpiderClassChecker.performCheckAndCollect(clazz);
            if (tuple != null) {
                SpiderMethodManager.put(clazz, tuple);
            }
        }

    }

    public boolean ipSwitched(IPProxyTool.Proxy proxy) {
        return false;
    }

    public void success(Runnable runnable) {
        dispatchedSpiderList.remove((BaseSpider) runnable);
        blockHelper.removeFail(runnable);
        failureSpiderList.remove((BaseSpider) runnable);

        successSpiderList.add((BaseSpider) runnable);
    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail) {
        fail(runnable, fail, true);
    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail, boolean retry) {

        //若是在switch ip,那么什么都不做
        if (!isSwitchingIP.get()) {
            blockHelper.addFail(runnable, fail);
            dispatchedSpiderList.remove(runnable);

            if (retry && JobManagerConfig.enableRetrySpider) {
                IJob next = JobProvider.getJob();//getNextJob(job);
                next.setRealRunnable(runnable);
                queue.putJob(next, IJob.STATE_RETRY);
            }

            if (blockHelper.isBlocked()) {
                LogManager.error("WE ARE BLOCKED!");  //Todo:日志打点
                heartbeatManager.stopHeartBeat();
                dealBlock();
            }
        }

    }

    public boolean putJob(@NotNull IJob job) {
        return queue.putJob(job, IJob.STATE_INIT);
    }

    public IJob getJob() {
        IJob job = queue.getJob();
        return job;
    }

    public void onDispatch(@NotNull BaseSpider spider) {
        dispatchedSpiderList.add(spider);
    }

    public void start() {
        if (!started) {
            started = true;
            heartbeatManager.addHeartBeatCallback(this);
            LogManager.info("WorkThread started");
            monitor.recordStartTime();
            workThread.start();
        }
    }

    private void dealBlock() {
        if (JobManagerConfig.enableSwitchProxy) {
            LogManager.info("We begin to switch IP...");
            doSwitchIp();
        } else {        //被block后就停止了 --> 主要用于测试
            isSwitchingIP.set(true);

            LogManager.info("We will not switch IP ");
            dispatcher.cancelAll();
            workThread.pauseWhenSwitchIP();
        }
    }

    //Todo: 重构
    private void doSwitchIp() {
        isSwitchingIP.set(true);
        workThread.pauseWhenSwitchIP();
        LogManager.info("WorkThread paused");
        dispatcher.cancelAll();

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

            //LogManager.info("We try to switch to Ip: " + proxy.ip + " Port: " + proxy.port);
            int ensure = 0;
            boolean success = false;
            while (!(success = ipSwitched(proxy)) && ensure < JobManagerConfig.everyProxyTryTime) {  //每个IP尝试三次
                ensure++;
            }
            if (success) {
                //LogManager.info("Switch Proxy success");
                break;
            }
        }

        dispatcher.cancelAll();
        for (BaseSpider spider : dispatchedSpiderList) {
            IJob job = JobProvider.getJob();
            job.setRealRunnable(spider);
            if (job != null) {
                queue.putJob(job, IJob.STATE_INIT);
            }
        }
        dispatchedSpiderList.clear();

        blockHelper.reInit();
        monitor.recordStartTime();
        LogManager.info("WorkThread resumed");
        workThread.resumeNow();
        isSwitchingIP.set(false);
    }


    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void onHeartBeatBegin() {
        //暂时不用做什么
    }

    public void onHeartBeat(int time) {
        LogManager.info("onHeartBeat " + time);
    }


    public void onHeartBeatFail() {
        LogManager.info("onHeartBeatFail");
        dealBlock();  //代理失效 --> 等同于被block
    }

    //JobManager主动调用HeartbeatManager.stopxxx
    public void onHeartBeatInterrupt() {
        LogManager.info("onHeartBeatInterrupt");
    }
}
