package wuxian.me.lagouspider.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.job.IJob;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 7/4/2017.
 * 记录所有状态的job --> 防止一个job被多次重复
 */
public class JobMonitor {

    private FailureMonitor failureMonitor = FailureMonitor.getInstance();
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


    public void success(Runnable runnable) {
        IJob job = getJob(runnable);
        if (job != null) {
            failureMonitor.success(job);

            job.setCurrentState(IJob.STATE_SUCCESS);
            putJob(job, IJob.STATE_SUCCESS);
        }
    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail) {
        IJob job = getJob(runnable);
        if (job != null) {
            failureMonitor.fail(job, fail);

            job.fail(fail);
            job.setCurrentState(IJob.STATE_FAIL);
            JobMonitor.getInstance().putJob(job, IJob.STATE_FAIL);  //本次失败了

            if (job.getFailTimes() >= IJob.MAX_FAIL_TIME) {  //ignore this
                return;
            }

            IJob next = JobProvider.getNextJob(job);              //重新制定爬虫策略 放入jobQueue
            next.setCurrentState(IJob.STATE_RETRY);
            JobQueue.getInstance().putJob(next);
            putJob(next, IJob.STATE_RETRY);
        }
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
}
