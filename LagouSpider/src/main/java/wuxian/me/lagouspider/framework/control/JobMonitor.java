package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.framework.job.IJob;

import java.util.HashMap;
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

    public JobMonitor() {
    }

    public void putJob(@NotNull IJob job, int state) {
        job.setCurrentState(state);
        set.put(job, state);  //Fixme:目前的设计(job.equals全都delegate给了runnable(spider).equals)会导致bug:
        // 没办法更改key的状态 也就是job的状态
        //所以我print状态的时候不能用job里的state,必须用set的value值
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

    private String getClassNameOfJob(@NotNull IJob job) {
        return job.getRealRunnable().getClass().getSimpleName();
    }

    private void increaseNum(@NotNull Map<String, Integer> map, @NotNull String key) {
        if (!map.containsKey(key)) {
            map.put(key, 1);
        } else {
            int num = map.get(key);
            map.put(key, num + 1);
        }
    }

    private String printMap(Map<String, Integer> map) {
        StringBuilder builder = new StringBuilder("");
        for (String str : map.keySet()) {
            builder.append(str + ":" + map.get(str) + " ,");
        }
        return builder.toString();
    }

    public String printAllJobStatus() {
        Map<String, Integer> init = new HashMap<String, Integer>();
        Map<String, Integer> success = new HashMap<String, Integer>();
        Map<String, Integer> fail = new HashMap<String, Integer>();
        Map<String, Integer> retry = new HashMap<String, Integer>();

        synchronized (set) {
            for (IJob job : set.keySet()) {
                String name = getClassNameOfJob(job);
                switch (set.get(job)) {
                    case IJob.STATE_INIT:
                        increaseNum(init, name);
                        break;
                    case IJob.STATE_SUCCESS:
                        increaseNum(success, name);
                        break;
                    case IJob.STATE_FAIL:
                        increaseNum(fail, name);
                        break;
                    case IJob.STATE_RETRY:
                        increaseNum(retry, name);
                        break;

                }
            }
            return "Init Jobs: " + printMap(init) + " Success Jobs: " + printMap(success)
                    + " Fail Jobs: " + printMap(fail) + " Retry Jobs: " + printMap(retry);
        }
    }
}
