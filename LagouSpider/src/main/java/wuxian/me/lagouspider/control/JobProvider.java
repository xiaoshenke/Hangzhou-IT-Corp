package wuxian.me.lagouspider.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.job.DelayJob;
import wuxian.me.lagouspider.job.IJob;
import wuxian.me.lagouspider.job.ImmediateJob;

import java.util.Random;

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

    public static IJob getDelayJob(long delay) {
        return new DelayJob(delay);
    }

    public static IJob getJob() {
        if (Config.IS_TEST) {
            return new ImmediateJob();
        }

        if (random.nextDouble() * 100 > 20) {
            return new DelayJob((long) random.nextDouble() * 1000 + 200);
        }
        return new ImmediateJob();
    }
}
