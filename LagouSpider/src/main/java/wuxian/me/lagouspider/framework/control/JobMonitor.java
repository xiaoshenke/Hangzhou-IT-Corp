package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.framework.job.IJob;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 7/4/2017.
 *
 * 保存了所有Job的状态 --> 可以看作是一张监视表
 *
 * 现在的做法是这样的
 * 1 放到@JobQueue队列里的时候 无需调用JobMonitor.put
 * 2 只有Job Success,Fail的时候应该更新JobMonitor里的job状态 --> 这个设计是不是有点不大合理...
 * 3 由JobMonitor来判断"这个job"是不是需要重试,由FailureManager来判断整个爬虫是不是被发现并且被屏蔽了
 *
 */
public class JobMonitor {

    private FailureManager failureMonitor = FailureManager.getInstance();
    private Map<IJob, Integer> set = new ConcurrentHashMap<IJob, Integer>();

    private Map<Runnable, IJob> jobMap = new ConcurrentHashMap<Runnable, IJob>();

    private JobMonitor() {
    }

    private static JobMonitor instance;

    public static JobMonitor getInstance() {
        if (instance == null) {
            instance = new JobMonitor();
        }
        return instance;
    }

    public void register(@NotNull BaseSpider spider) {
        failureMonitor.register(spider);
    }

    public void success(Runnable runnable) {
        IJob job = getJob(runnable);
        if (job != null) {
            job.setCurrentState(IJob.STATE_SUCCESS);
            putJob(job, IJob.STATE_SUCCESS);

            failureMonitor.success(job);
        }
    }

    //Fail并且判断是否应该进行job重试 若是则放入重试队列
    public void fail(@NotNull Runnable runnable, @NotNull Fail fail, boolean retry) {
        IJob job = getJob(runnable);
        if (job != null) {
            job.setCurrentState(IJob.STATE_FAIL);
            job.fail(fail);
            putJob(job, IJob.STATE_FAIL);  //更新状态

            failureMonitor.fail(job, fail);

            if (job.getFailTimes() >= Config.SINGLEJOB_MAX_FAIL_TIME) { //是否进行重试
                logger().error("Job: " + job.toString() + " fail " + job.getFailTimes() + "times, abandon it");
                return;
            }

            if (!Config.ENABLE_RETRY_SPIDER || !retry) {
                logger().error("Job: " + job.toString() + " fail, will not retry");
                return;
            }

            logger().info("Retry Job: " + job.toString());

            IJob next = JobProvider.getNextJob(job);  //重新制定爬虫策略 放入jobQueue

            next.setCurrentState(IJob.STATE_RETRY);
            JobQueue.getInstance().putJob(next, IJob.STATE_RETRY);
        }
    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail) {
        fail(runnable, fail, true);
    }

    public void putJob(@NotNull IJob job, int state) {
        set.put(job, state);
        jobMap.put(job.getRealRunnable(), job);
    }

    public IJob getJob(@NotNull Runnable runnable) {
        if (jobMap.containsKey(runnable)) {
            return jobMap.get(runnable);
        }
        return null;
    }

    public int getWholeJobNum() {
        return set.size();
    }
}
