package wuxian.me.lagouspider;

import wuxian.me.lagouspider.biz.boss.BizConfig;
import wuxian.me.lagouspider.biz.lagou.PositionSpider;
import wuxian.me.lagouspider.model.lagou.Position;
import wuxian.me.lagouspider.save.lagou.PositionSaver;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;
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

        ModuleProvider.positionMapper().createNewTableIfNeed(new Position());

        JobManagerFactory.getJobManager().start();

        SignalManager.registerOnSystemKill(PositionSaver.getInstance());

        try {
            Thread.sleep(10000);  //Fixme:spidersdk的bug 需要sleep一段时间
        } catch (InterruptedException e) {

        }

        LogManager.info("begin to dispatch spider");
        PositionSpider spider = new PositionSpider("西湖区", 1);  //Todo: toUrlNode..
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
