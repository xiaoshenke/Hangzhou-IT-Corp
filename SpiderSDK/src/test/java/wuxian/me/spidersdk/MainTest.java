package wuxian.me.spidersdk;

import org.junit.Test;
import wuxian.me.spidersdk.anti.IPProxyTool;
import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.manager.JobManagerFactory;
import wuxian.me.spidersdk.manager.PlainJobManager;
import wuxian.me.spidersdk.util.ProcessUtil;
import wuxian.me.spidersdk.util.ShellUtil;

import java.util.Set;

/**
 * Created by wuxian on 12/5/2017.
 */
public class MainTest {

    static {
        Main.findCorrectFilePath();
    }

    @Test
    public void testIpproxyTool() {
        //new IPProxyTool();
        JobManagerFactory.getJobManager().start();
    }

    @Test
    public void testScanPackage() {
        System.out.println("begin to scan");
        Set<Class<?>> classSet = ClassHelper.getSpiderFromPackage(JobManagerConfig.spiderScan);

        if (classSet != null) {
            for (Class clazz : classSet) {
                System.out.println(clazz);
            }
        }
    }

    @Test
    public void testPackageValid() {
        String str = "com.wu.133ma";
        System.out.println(ClassHelper.isPackageStringValid(str));

        str = "com.wu.122.";
        System.out.println(ClassHelper.isPackageStringValid(str));

        str = "com...wu.122";
        System.out.println(ClassHelper.isPackageStringValid(str));

        str = "&dfa.wu.12";
        System.out.println(ClassHelper.isPackageStringValid(str));

    }

    @Test
    public void testCurrentProcessId() {
        ShellUtil.init();
        System.out.println("begin");
        System.out.println(ShellUtil.killProcessBy(ProcessUtil.getCurrentProcessId()));
        while (true) {
            ;
        }
    }


    @Test
    public void testIpPort() {
        String[] str1 = new String[]{"49.79.57.145", "49257"};
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
        JobManagerFactory.getJobManager().start();
        JobManagerFactory.getJobManager().putJob(job);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        job = JobManagerFactory.getJobManager().getJob();

        LogManager.info("job: " + job);

        while (true) {

        }

    }

}