package wuxian.me.lagouspider.framework.job;

/**
 * Created by wuxian on 31/3/2017.
 */
public class ImmediateJob extends BaseJob {

    public void run() {
        if (realJob != null) {
            realJob.run();
        }
    }
}
