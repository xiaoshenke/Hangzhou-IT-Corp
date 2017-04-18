package wuxian.me.lagouspider.framework.control;

import wuxian.me.lagouspider.framework.job.IJob;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 1/4/2017.
 *
 * 任务队列:这个任务队列是要进行的任务队列,任务一旦开始后就会被踢出并被保存在@JobResultManager.todoSpiderList中
 *
 * 所有任务的状态会被更新到@JobMonitor
 */
public class JobQueue {

    private static JobQueue instance = new JobQueue();

    public static JobQueue getInstance() {
        return instance;
    }

    BlockingQueue<IJob> queue = new LinkedBlockingQueue();

    private JobQueue() {
    }

    public boolean putJob(IJob job, int state) {
        logger().debug("putJob: " + job.toString());

        JobMonitor.getInstance().putJob(job, state);

        return queue.offer(job);
    }

    public boolean putJob(IJob job) {
        return putJob(job, IJob.STATE_INIT);
    }

    public IJob getJob() {
        IJob job = queue.poll();
        if (job == null) {
            logger().debug("getJob: jobQueue empty");
        } else {
            logger().debug("getJob: " + job.toString());
        }
        return job;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int getJobNum() {
        return queue.size();
    }
}
