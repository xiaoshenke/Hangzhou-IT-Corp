package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.framework.job.DelayJob;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.framework.job.ImmediateJob;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * usage:
 * 1 IJob job = JobProvider.getNextJob();
 * 2 job.setRealRunnable(real-runnable);
 * 3 job.run();
 * <p>
 */
public class JobProvider {
    private JobProvider() {
    }

    private static Random random = new Random();

    private static AtomicInteger sindex = new AtomicInteger(0);

    public static IJob getNextJob(@NotNull IJob job) {
        if (Config.USE_FIXED_DELAY_NEXT_JOB) {
            return getFixedDelayNextJob(job);
        }

        return job; //Todo: better stratogy？？
    }

    private static IJob getFixedDelayNextJob(@NotNull IJob job) {
        return getFixedDelayJob(Config.FIXED_DELAYJOB_INTERVAL, job);
    }

    private static IJob getFixedDelayJob(long delay, @NotNull IJob job) {
        if (delay == 0) {
            return new ImmediateJob();
        }

        IJob job1 = new DelayJob(sindex.get() * delay);
        job1.setRealRunnable(job.getRealRunnable());
        sindex.set(sindex.get() + 1);
        return job1;
    }

    public static IJob getJob() {
        if (Config.USE_FIXED_DELAY_JOB) {
            return getFixedDelayJob();
        }

        return new ImmediateJob(); //Todo: better stratogy？？
    }

    private static IJob getFixedDelayJob() {
        return getFixedDelayJob(Config.FIXED_DELAYJOB_INTERVAL);
    }

    private static IJob getFixedDelayJob(long delay) {
        if (delay == 0) {
            return new ImmediateJob();
        }

        IJob job = new DelayJob(sindex.get() * delay);
        sindex.set(sindex.get() + 1);
        return job;
    }
}
