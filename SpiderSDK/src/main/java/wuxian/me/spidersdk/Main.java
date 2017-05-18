package wuxian.me.spidersdk;

import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.log.LogManager;
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
                file = new File(FileUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                file = file.getParentFile();
                FileUtil.setCurrentPath(file.getAbsolutePath());
            } catch (Exception e) {

            }
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

        LogManager.info("job: " + job);
    }
}
