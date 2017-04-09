package wuxian.me.lagouspider.control;

import wuxian.me.lagouspider.job.IJob;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

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
        logger().info("putJob: " + job.toString());
        return queue.offer(job);
    }

    public IJob getJob() {
        IJob job = queue.poll();
        if (job == null) {
            logger().info("getJob: jobQueue empty");
        } else {
            logger().info("getJob: " + job.toString());
        }
        return job;
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
