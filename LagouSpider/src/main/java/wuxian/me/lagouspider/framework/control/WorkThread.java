package wuxian.me.lagouspider.framework.control;

import wuxian.me.lagouspider.framework.job.IJob;

/**
 * Created by wuxian on 6/4/2017.
 *
 * 由@JobManager管理
 */
public class WorkThread extends Thread {
    private static final long SLEEP_TIME = 1000;
    private JobManager jobManager = JobManager.getInstance();

    public WorkThread() {
        ;
    }

    private boolean pause = false;

    public void pauseWhenSwitchIP() {
        pause = true;
    }

    public synchronized void resumeNow() {
        pause = false;
        notifyAll();
    }

    private synchronized boolean doIfShouldWait() {
        if (pause) {
            try {
                wait();
            } catch (InterruptedException e) {
                ;
            }
        }
        return true;
    }

    @Override
    public void run() {
        while (true) {
            while (!jobManager.isEmpty()) {
                doIfShouldWait();
                IJob job = jobManager.getJob();
                job.run();
            }

            try {
                sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                ;  //ignore
            }
        }
    }
}
