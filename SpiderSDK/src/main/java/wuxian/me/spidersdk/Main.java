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

    public Main() {
        ;
    }

    public void init() {

        LogManager.info("Main_static Begin.");
        LogManager.info("1 Find Current File Path.");

        FileUtil.initWithClass(Main.class);
        LogManager.info("Current Path." + FileUtil.getCurrentPath());

        JobManagerConfig.init();

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

    public void start() {
        NoneSpider spider = new NoneSpider();
        IJob job = JobProvider.getJob();
        job.setRealRunnable(spider);
        JobManagerFactory.getJobManager().start();
        JobManagerFactory.getJobManager().putJob(job);
    }

    public static void main(String[] args) {

        Main main = new Main();
        main.init();
        main.start();
    }
}
