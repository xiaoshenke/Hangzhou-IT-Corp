package wuxian.me.lagouspider;

import wuxian.me.lagouspider.job.IJob;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wuxian on 1/4/2017.
 */
public class JobQueue {

    private static JobQueue instance = new JobQueue();

    public static JobQueue getInstance() {
        return instance;
    }

    BlockingQueue<IJob> queue = new LinkedBlockingQueue();

    private JobQueue() {
    }

    public boolean putJob(IJob job) {
        return queue.offer(job);
    }

    public IJob getJob() {
        return queue.poll();
    }

    public boolean contains(IJob job) {
        return queue.contains(job);
    }

    public void removeAllJob() {
        queue.clear();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
