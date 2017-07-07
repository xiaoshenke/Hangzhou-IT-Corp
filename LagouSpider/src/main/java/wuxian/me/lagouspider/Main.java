package wuxian.me.lagouspider;

import wuxian.me.lagouspider.biz.boss.BPositionListSpider;
import wuxian.me.lagouspider.biz.boss.BizConfig;
import wuxian.me.lagouspider.biz.lagou.PositionSpider;
import wuxian.me.lagouspider.save.boss.BCompanySaver;
import wuxian.me.lagouspider.save.boss.BLocationSaver;
import wuxian.me.lagouspider.save.boss.BPositionSaver;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidercommon.log.ILog;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.util.FileUtil;
import wuxian.me.spidercommon.util.SignalManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.manager.JobManagerFactory;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 20/5/2017.
 */
public class Main {

    public static void init() {
        LogManager.info("Main_static Begin.");

        JobManagerConfig.init();

        LogManager.info("Current Path: "+ FileUtil.getCurrentPath());

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


        LogManager.info("Main_static End.");

        BizConfig.init();
    }

    public void run() {
        JobManagerFactory.getJobManager().start();

        SignalManager.registerOnSystemKill(BPositionSaver.getInstance());
        SignalManager.registerOnSystemKill(BCompanySaver.getInstance());
        SignalManager.registerOnSystemKill(BLocationSaver.getInstance());

        //如果想开启抓一个新的区域 那么只需填入下面的xxx即可
        //BPositionListSpider spider = new BPositionListSpider("西湖区",1);

        PositionSpider spider = new PositionSpider("西湖区",1);
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
