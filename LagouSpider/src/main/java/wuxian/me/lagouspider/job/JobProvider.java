package wuxian.me.lagouspider.job;

import wuxian.me.lagouspider.util.Helper;

import java.util.Random;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * usage:
 * 1 IJob job = JobProvider.getJob();
 * 2 job.setRealRunnable(real-runnable);
 * 3 job.run();
 */
public class JobProvider {
    private JobProvider() {
    }

    private static Random random = new Random();

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
