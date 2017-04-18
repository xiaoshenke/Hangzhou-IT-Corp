package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.framework.job.IJob;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 7/4/2017.
 * 保存了所有Job的状态 --> 监视表
 */
public class JobMonitor {

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

    public boolean contains(@NotNull IJob job) {
        return set.containsKey(job);
    }

    public int getWholeJobNum() {
        return set.size();
    }
}
