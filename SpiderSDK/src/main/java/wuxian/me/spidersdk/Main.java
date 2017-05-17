package wuxian.me.spidersdk;

import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.distribute.SpiderClassChecker;
import wuxian.me.spidersdk.distribute.SpiderMethodTuple;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.util.FileUtil;

import java.io.File;

/**
 * Created by wuxian on 12/5/2017.
 */
public class Main {

    static {
        File file = null;
        try {
            file = new File(FileUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            file = file.getParentFile();
            FileUtil.setCurrentPath(file.getAbsolutePath());
        } catch (Exception e) {

        }

    }

    public static void main(String[] args) {

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

        /*
        NoneSpider spider = new NoneSpider();
        System.out.println("Hello World,This Is Main.main");
        System.out.println("redis enabled: " + JobManagerConfig.useRedisJobQueue);

        try {
            Class clazz = ClassHelper.getClassByName("wuxian.me.spidersdk.NoneSpider");
            SpiderMethodTuple tuple = SpiderClassChecker.performCheckAndCollect(clazz);
            System.out.println("success");
        } catch (ClassNotFoundException e) {

        }

        JobManager.getInstance(); //会进行BaseSpider check
        */
    }
}
