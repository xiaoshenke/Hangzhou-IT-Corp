package wuxian.me.lagouspider.strategy;

/**
 * Created by wuxian on 31/3/2017.
 */
public class ImmediateStrategy extends BaseStrategy {

    public void run() {
        if (realJob != null) {
            realJob.run();
        }
    }
}
