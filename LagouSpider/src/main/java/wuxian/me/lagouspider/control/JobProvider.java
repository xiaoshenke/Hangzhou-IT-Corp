package wuxian.me.lagouspider.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.job.DelayJob;
import wuxian.me.lagouspider.job.IJob;
import wuxian.me.lagouspider.job.ImmediateJob;
import wuxian.me.lagouspider.util.Helper;

import java.util.Random;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * usage:
 * 1 IJob job = JobProvider.getNextJob();
 * 2 job.setRealRunnable(real-runnable);
 * 3 job.run();
 */
public class JobProvider {
    private JobProvider() {
    }

    private static Random random = new Random();

    //Todo:读取失败的数据 根据失败原因制定重试策略
    public static IJob getNextJob(@NotNull IJob job) {
        return job;
    }

    public static IJob getJob() {
        if (Helper.isTest) {
            return new ImmediateJob();
        }
        if (random.nextDouble() * 100 > 30) {
            return new DelayJob();
        }
        return new ImmediateJob();
    }
}
