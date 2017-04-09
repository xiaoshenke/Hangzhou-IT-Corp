package wuxian.me.lagouspider.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.job.DelayJob;
import wuxian.me.lagouspider.job.IJob;
import wuxian.me.lagouspider.job.ImmediateJob;

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
 * Todo: 确定策略
 * Todo: 跑几个单元测试
 */
public class JobProvider {
    private JobProvider() {
    }

    private static Random random = new Random();

    public static IJob getNextJob(@NotNull IJob job) {
        if (Config.IS_TEST) {
            return job;
        }

        int time = job.getFailTimes();
        IJob next = new DelayJob(time * time * 1000);
        next.setRealRunnable(job.getRealRunnable());
        return next;
    }

    private static AtomicInteger sindex = new AtomicInteger(0);

    public static IJob getFixedDelayJob(long delay) {
        if (delay == 0) {
            return new ImmediateJob();
        }

        IJob job = new DelayJob(sindex.get() * delay);
        sindex.set(sindex.get());
        return job;
    }

    public static IJob getDelayJob(long delay) {
        return new DelayJob(delay);
    }

    public static IJob getJob() {
        if (random.nextDouble() * 100 > 20) {
            return new DelayJob((long) random.nextDouble() * 1000 + 200);
        }
        return new ImmediateJob();
    }
}
