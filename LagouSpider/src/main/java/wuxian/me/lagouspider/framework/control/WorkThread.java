package wuxian.me.lagouspider.framework.control;

import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.framework.job.IJob;

import java.util.Random;

/**
 * Created by wuxian on 6/4/2017.
 *
 * 由@JobManager管理
 *
 */
public class WorkThread extends Thread {
    private JobManager jobManager;

    private int i = 0;
    private Random random = new Random();

    public WorkThread(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    private boolean pause = false;

    public void pauseWhenSwitchIP() {
        pause = true;
    }

    public synchronized void resumeNow() {
        pause = false;

        i = 0;
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
                if (i >= Config.JobScheduler.SWITCH_SLEEP_JOB_NUMBER) {  //每隔10个任务休息10s
                    try {
                        sleep(Config.JobScheduler.SWITCH_SLEEP_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        ;
                    }
                    doIfShouldWait();
                    IJob job = jobManager.getJob();
                    job.run();
                    i = 0;
                } else {
                    i++;
                    int min = Config.JobScheduler.SLEEP_TIME_MIN;
                    int max = Config.JobScheduler.SLEEP_TIME_MAX;
                    int sleepTime = (int) (min + random.nextDouble() * (max - min)) * 1000;
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {

                    }

                    doIfShouldWait();
                    IJob job = jobManager.getJob();
                    job.run();
                }
            }

            try {
                sleep(Config.JobScheduler.SLEEP_WHEN_QUEUE_EMPTY);
            } catch (InterruptedException e) {
                ;  //ignore
            }
        }
    }
}
