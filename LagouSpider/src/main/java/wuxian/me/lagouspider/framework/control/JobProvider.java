package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.framework.job.ImmediateJob;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * usage:
 * 1 IJob job = JobProvider.getNextJob();
 * 2 job.setRealRunnable(real-runnable);
 * 3 job.run();
 * <p>
 *
 * delay逻辑放到@workThread了
 */
public class JobProvider {
    private JobProvider() {
    }

    public static IJob getNextJob(@NotNull IJob job) {
        return job;
    }

    public static IJob getJob() {
        return new ImmediateJob();
    }
}
