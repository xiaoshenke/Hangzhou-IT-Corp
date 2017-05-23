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

    public static void findCorrectFilePath() {
        try {
            File file = new File(Main.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().getPath());
            if (FileUtil.checkFileExist(file.getParentFile().getAbsolutePath() + "/conf/jobmanager.properties")) {
                FileUtil.setCurrentFile(file.getAbsolutePath());
                FileUtil.setCurrentPath(file.getParentFile().getAbsolutePath());
                return;
            }
        } catch (Exception e) {
            ;
        }

        File file = new File("");
        FileUtil.setCurrentPath(file.getAbsolutePath());
    }

    static {
        LogManager.info("Main_static Begin.");
        LogManager.info("1 Find Current File Path.");
        findCorrectFilePath();
        LogManager.info("Current Path." + FileUtil.getCurrentPath());

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
