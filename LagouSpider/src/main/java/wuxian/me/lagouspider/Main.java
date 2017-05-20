package wuxian.me.lagouspider;

import wuxian.me.lagouspider.util.Config;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.log.ILog;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.manager.JobManagerFactory;
import wuxian.me.spidersdk.util.FileUtil;

import java.io.File;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 20/5/2017.
 */
public class Main {

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

    static {
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
            @Override
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

    }

    public void run() {
        LogManager.info("Run");
    }

    public static void main(String[] args) {
        LogManager.info("Main Begin");
        Main main = new Main();
        main.run();
        LogManager.info("Main End");

        while (true) {

        }
    }
}
