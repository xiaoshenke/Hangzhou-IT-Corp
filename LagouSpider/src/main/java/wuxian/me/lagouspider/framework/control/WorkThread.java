package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.framework.job.IJob;

/**
 * Created by wuxian on 6/4/2017.
 *
 * 由@JobManager管理
 *
 * Fixme: 现在的设计是延迟任务由DelayJob控制
 * 觉得是不是应该把delay直接放到WorkThread来做？减少复杂度
 */
public class WorkThread extends Thread {
    private static final long SLEEP_TIME = 1000;
    private JobManager jobManager;// = JobManager.getInstance();

    public WorkThread(JobManager jobManager) {
        this.jobManager = jobManager;
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
