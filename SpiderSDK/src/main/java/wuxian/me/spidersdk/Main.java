package wuxian.me.spidersdk;

import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.manager.DistributeJobManager;
import wuxian.me.spidersdk.manager.JobManagerFactory;
import wuxian.me.spidersdk.manager.PlainJobManager;
import wuxian.me.spidersdk.util.FileUtil;

import java.io.File;

/**
 * Created by wuxian on 12/5/2017.
 */
public class Main {

    static {
        if (JobManagerConfig.jarMode) {
            File file = null;
            try {
                file = new File(FileUtil.class.getProtectionDomain().getCodeSource().
                        getLocation().toURI().getPath());
                file = file.getParentFile();
                FileUtil.setCurrentPath(file.getAbsolutePath());
            } catch (Exception e) {

            }
        }

        if (JobManagerConfig.distributeMode) {
            JobManagerFactory.initCheckFilter(new ClassHelper.CheckFilter() {
                public boolean apply(String s) {
                    boolean ret = true;
                    if (s.contains("org/")) {
                        ret = false;
                    } else if (s.contains("google")) {
                        ret = false;
                    } else if (s.contains("squareup")) {
                        ret = false;
                    }
                    return ret;
                }
            });
        }
    }

    public static void main(String[] args) {

        LogManager.info("Main");

        NoneSpider spider = new NoneSpider();
        IJob job = JobProvider.getJob();
        job.setRealRunnable(spider);
        //((DistributeJobManager) JobManagerFactory.getJobManager()).getDispatchedSpiderList().add(spider);
        JobManagerFactory.getJobManager().start();
        JobManagerFactory.getJobManager().putJob(job);

        while (true) {

        }
        /*
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
        */
    }
}
