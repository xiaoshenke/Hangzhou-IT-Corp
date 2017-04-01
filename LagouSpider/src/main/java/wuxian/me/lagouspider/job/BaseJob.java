package wuxian.me.lagouspider.job;

import com.sun.istack.internal.NotNull;

/**
 * Created by wuxian on 31/3/2017.
 */
public abstract class BaseJob implements IJob {

    protected Runnable realJob;

    public final void setRealRunnable(@NotNull Runnable runnable) {
        realJob = runnable;
    }
}
