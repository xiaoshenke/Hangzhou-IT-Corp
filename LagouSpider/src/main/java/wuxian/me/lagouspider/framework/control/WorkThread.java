package wuxian.me.lagouspider.framework.control;
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

                //不使用任何策略 立即分发job模式
                if (jobManager.getConfig().enableScheduleImmediately) {
                    doIfShouldWait();
                    IJob job = jobManager.getJob();
                    job.run();
                    continue;
                }
                if (i >= jobManager.getConfig().jobNumToSleep) {  //每隔10个任务休息10s
                    try {
                        sleep(jobManager.getConfig().jobSleepTimeToSleep);
                    } catch (InterruptedException e) {
                        ;
                    }
                    doIfShouldWait();
                    IJob job = jobManager.getJob();
                    job.run();
                    i = 0;
                } else {
                    i++;
                    int min = jobManager.getConfig().jobSchedulerTimeMin;
                    int max = jobManager.getConfig().jobSchedulerTimeMax;
                    int sleepTime = (int) (min + random.nextDouble() * (max - min)) * 1000;
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {

                    }

                    doIfShouldWait();
                    IJob job = jobManager.getJob();
                    try {
                        job.run();
                    } catch (IllegalArgumentException e) {
                        //ignore
                    }

                }
            }

            try {
                sleep(jobManager.getConfig().jobQueueEmptySleepTime);
            } catch (InterruptedException e) {
                ;  //ignore
            }
        }
    }
}
