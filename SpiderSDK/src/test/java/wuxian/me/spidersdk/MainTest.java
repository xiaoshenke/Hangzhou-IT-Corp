package wuxian.me.spidersdk;

import org.junit.Test;
import wuxian.me.spidersdk.anti.IPProxyTool;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.manager.JobManagerFactory;
import wuxian.me.spidersdk.manager.PlainJobManager;

/**
 * Created by wuxian on 12/5/2017.
 */
public class MainTest {

    @Test
    public void testIpPort() {
        String[] str1 = new String[]{"12.0.0.3", "313"};
        System.out.println(IPProxyTool.isVaildIpPort(str1));

        String[] str2 = new String[]{"12.0.0.3", "3dfad"};
        System.out.println(IPProxyTool.isVaildIpPort(str2));

        String[] str3 = new String[]{"12.0.0.3.", "313"};
        System.out.println(IPProxyTool.isVaildIpPort(str3));

        String[] str4 = new String[]{"12.0.0.a", "313"};
        System.out.println(IPProxyTool.isVaildIpPort(str4));

    }

    @Test
    public void testRedisJobQueue() {

        IJob job = JobProvider.getJob();
        NoneSpider spider = new NoneSpider();
        job.setRealRunnable(spider);
        JobManagerFactory.getJobManager().putJob(job);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        job = JobManagerFactory.getJobManager().getJob();

        LogManager.info("job: " + job);

        //SpiderClassChecker.performCheckAndCollect(NoneSpider.class);


    }

}