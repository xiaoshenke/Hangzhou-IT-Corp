package wuxian.me.lagouspider.job;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.control.Fail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 31/3/2017.
 */
public abstract class BaseJob implements IJob {

    private List<Fail> fails;  //根据每次失败的情况决定反爬虫策略

    public final void fail(@NotNull Fail fail) {
        if (fails == null) {
            fails = new ArrayList<Fail>();
        }
        fails.add(fail);
    }

    public final int getFailTimes() {
        return fails == null ? 0 : fails.size();
    }

    private int state = STATE_INIT;

    public final int getCurrentState() {
        return state;
    }

    public final void setCurrentState(int state) {
        this.state = state;
    }

    protected Runnable realJob;

    public final void setRealRunnable(@NotNull Runnable runnable) {
        realJob = runnable;
    }

    @Override
    public int hashCode() {
        if (realJob != null) {
            return realJob.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof BaseJob) {
            return realJob.equals(((BaseJob) obj).realJob);
        }
        return super.equals(obj);
    }
}
