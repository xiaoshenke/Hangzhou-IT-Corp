package wuxian.me.lagouspider.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.job.IJob;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 7/4/2017.
 * 记录所有状态的job --> 防止一个job被多次重复
 */
public class WholeJob {

    private Map<IJob, Integer> set = new ConcurrentHashMap<IJob, Integer>();

    private Map<Runnable, IJob> jobMap = new ConcurrentHashMap<Runnable, IJob>();

    private WholeJob() {
    }

    private static WholeJob instance;

    public static WholeJob getInstance() {
        if (instance == null) {
            instance = new WholeJob();
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
        return set.keySet().contains(job);
    }

    public void clear() {
        set.clear();
    }
}
