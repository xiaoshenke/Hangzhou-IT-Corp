package wuxian.me.lagouspider.job;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wuxian on 31/3/2017.
 */
public class DelayJob extends BaseJob {

    private static Timer timer = new Timer();

    TimerTask timerTask;

    public void run() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (realJob != null) {
                        realJob.run();
                    }
                }
            };
        }

        timer.schedule(timerTask, 1000);  //Todo:延迟时间策略
    }
}
