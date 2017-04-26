package wuxian.me.lagouspider.framework.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.framework.job.IJob;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 1/4/2017.
 * <p>
 * 任务队列:这个任务队列是要进行的任务队列,
 * 任务一旦开始后会被踢出并被保存在@JobManager.todoSpiderList中
 * <p>
 * 所有任务的状态会被更新到@JobMonitor
 */
public class JobQueue {

    private JobMonitor monitor;
    private Random random = new Random();

    List<IJob> queue = new ArrayList<IJob>();

    public JobQueue(@NotNull JobMonitor monitor) {
        this.monitor = monitor;
    }

    public boolean putJob(IJob job, int state) {
        logger().debug("putJob: " + job.toString());

        //通过检查job防止重复:比如说重复进行company主页的抓取
        if (!Config.Queue.ENABLE_DUPLICATE_INSERT && monitor.contains(job) && state != IJob.STATE_RETRY) {
            return true;
        }
        monitor.putJob(job, state);

        synchronized (queue) {
            if (Config.Queue.ENABLE_RANDOM_INSERT) {
                if (queue.size() == 0) {
                    queue.add(job);
                } else {
                    //随机插入
                    int index = (int) random.nextDouble() * queue.size();
                    queue.add(index, job);
                }
            } else {
                queue.add(job);
            }

        }
        return true;
    }

    public boolean putJob(@NotNull IJob job) {
        return putJob(job, IJob.STATE_INIT);
    }

    public IJob getJob() {
        IJob job = null;
        synchronized (queue) {
            if (queue.isEmpty()) {
                return null;
            } else {
                job = queue.get(0);
                queue.remove(0);
            }

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
