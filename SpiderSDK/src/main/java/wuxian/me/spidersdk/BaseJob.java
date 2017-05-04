package wuxian.me.spidersdk;

import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.job.IJob;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * 目前BaseJob的设计是:hashCode,equals都是调用的BaseSpider
 * 这样的目的是为了能够统计某个Spider的失败次数。--> 然后这个值似乎并没有用上
 */
public abstract class BaseJob implements IJob {

    private List<Fail> fails;  //根据每次失败的情况决定反爬虫策略

    public final void fail(Fail fail) {
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

    public final void setRealRunnable(Runnable runnable) {
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
            return ret;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        if (realJob == null) {
            return "Invalid Job";
        }
        return "Job State: " + state + " " + realJob.toString();
    }
}