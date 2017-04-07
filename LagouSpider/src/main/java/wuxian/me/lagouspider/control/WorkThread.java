package wuxian.me.lagouspider.control;


import wuxian.me.lagouspider.job.IJob;

/**
 * Created by wuxian on 6/4/2017.
 */
public class WorkThread extends Thread {
    private static final long SLEEP_TIME = 1000;

    @Override
    public void run() {
        while (true) {
            while (!JobQueue.getInstance().isEmpty()) {
                IJob job = JobQueue.getInstance().getJob();
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
