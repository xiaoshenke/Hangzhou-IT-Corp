package wuxian.me.lagouspider.framework.job;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.framework.control.Fail;

import java.util.ArrayList;
import java.util.List;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

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

    public int getFailTimes() {
        return fails == null ? 0 : fails.size();
    }

    public final List<Fail> getFailures() {
        return fails;
    }

    private int state = STATE_INIT;

    public final int getCurrentState() {
        return state;
    }

    public final void setCurrentState(int state) {
        this.state = state;

        if (state == STATE_INIT) { //初始化一下
            if (fails != null) {
                fails.clear();
            }
        }
    }

    protected Runnable realJob;

    public final void setRealRunnable(@NotNull Runnable runnable) {
        realJob = runnable;
    }

    public final Runnable getRealRunnable() {
        return realJob;
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
            boolean ret = realJob.equals(((BaseJob) obj).realJob);
            logger().info("BaseJob equals return " + ret);
            return ret;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        if (realJob == null) {
            return "Invalid Job";
        }

        if (state == STATE_FAIL) {
            return "Job Fail,fail times: " + getFailTimes() + " " + realJob.toString();
        } else if (state == STATE_SUCCESS) {
            return "Job Success " + realJob.toString();
        } else if (state == STATE_INIT) {
            return "Job Inited: " + realJob.toString();
        }
        return "Job State: " + state + " " + realJob.toString();
    }
}
