package wuxian.me.spidersdk;

import org.junit.Test;
import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.distribute.SpiderClassChecker;
import wuxian.me.spidersdk.distribute.SpiderMethodTuple;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by wuxian on 12/5/2017.
 */
public class MainTest {


    @Test
    public void testRedisJobQueue() {

        IJob job = JobProvider.getJob();
        NoneSpider spider = new NoneSpider();
        job.setRealRunnable(spider);
        JobManager.getInstance().putJob(job);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        job = JobManager.getInstance().getJob();

        System.out.println("job: " + job);

        //SpiderClassChecker.performCheckAndCollect(NoneSpider.class);


    }

}