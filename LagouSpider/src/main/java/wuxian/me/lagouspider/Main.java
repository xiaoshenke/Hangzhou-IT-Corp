package wuxian.me.lagouspider;

import wuxian.me.lagouspider.biz.boss.BPositionListSpider;
import wuxian.me.lagouspider.biz.boss.BizConfig;
import wuxian.me.lagouspider.save.boss.BCompanySaver;
import wuxian.me.lagouspider.save.boss.BLocationSaver;
import wuxian.me.lagouspider.save.boss.BPositionSaver;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidercommon.log.ILog;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.util.FileUtil;
import wuxian.me.spidercommon.util.SignalManager;
import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.manager.JobManagerFactory;

import java.io.File;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 20/5/2017.
 */
public class Main {

    public static void init() {
        LogManager.info("Main_static Begin.");
        LogManager.info("1 Find Current File Path.");
        findCorrectFilePath();
        LogManager.info("Current Path." + FileUtil.getCurrentPath());

        LogManager.info("2 Init LogManager.");
        LogManager.info("3 Init ModuleProvider.");
        LogManager.setRealLogImpl(new ILog() {

            public void debug(String message) {
                logger().debug(message);
            }

            public void info(String message) {
                logger().info(message);
            }

            public void error(String message) {
                logger().error(message);
            }

            public void warn(String message) {
                logger().warn(message);
            }
        });

        LogManager.info("5 Init SpiderClassChecker.");
        JobManagerFactory.initCheckFilter(new ClassHelper.CheckFilter() {  //Fix 有的jar包里的类无法加载的问题
            public boolean apply(String s) {
                boolean ret = true;
                if(s.contains("org/")){
                    ret = false;
                } else if(s.contains("google")){
                    ret = false;
                }

                return ret;
            }
        });
        LogManager.info("Main_static End.");

        BizConfig.init();
    }

    //默认jar包运行 检查该路径下是否存在/conf/jobmanager.properties 配置文件,若有,读取配置
    private static void findCorrectFilePath() {
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

    public void run() {
        JobManagerFactory.getJobManager().start();
        //Todo
        //SignalManager.registerOnSystemKill(BPositionSaver.getInstance());
        //SignalManager.registerOnSystemKill(BCompanySaver.getInstance());
        //SignalManager.registerOnSystemKill(BLocationSaver.getInstance());

        //如果想开启抓一个新的区域 那么只需填入下面的xxx即可
        BPositionListSpider spider = new BPositionListSpider("西湖区",1);
        Helper.dispatchSpider(spider);
    }

    public static void main(String[] args) {
        init();
        LogManager.info("Main Begin");
        Main main = new Main();
        main.run();
        LogManager.info("Main End");

        while (true) {

        }
    }
}
