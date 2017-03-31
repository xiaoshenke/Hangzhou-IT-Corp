package wuxian.me.lagouspider.strategy;

import com.sun.istack.internal.NotNull;

/**
 * Created by wuxian on 31/3/2017.
 */
public abstract class BaseStrategy implements IStrategy {

    protected Runnable realJob;

    public final void setRunnable(@NotNull Runnable runnable) {
        realJob = runnable;
    }
}
