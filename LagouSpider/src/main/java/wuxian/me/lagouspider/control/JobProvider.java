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

    public static IJob getNextJob(@NotNull IJob job) {
        int time = job.getFailTimes();

        IJob next = new DelayJob(time * time * 1000);  //Fixme: Todo: a better strategy?
        next.setRealRunnable(job.getRealRunnable());

        return next;
    }

    public static IJob getJob() {

        if (random.nextDouble() * 100 > 20) {
            return new DelayJob((long) random.nextDouble() * 1000 + 200);
        }
        return new ImmediateJob();
    }
}
