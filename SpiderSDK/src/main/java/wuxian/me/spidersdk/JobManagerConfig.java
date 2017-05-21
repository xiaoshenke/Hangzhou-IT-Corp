package wuxian.me.spidersdk;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.util.FileUtil;
import wuxian.me.spidersdk.util.ShellUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by wuxian on 30/4/2017.
 */
public class JobManagerConfig {

    public static boolean distributeMode;

    public static String spiderIdentity;  //none,master,agent三种身份

    public static boolean newSpideMode;
    public static boolean enableSeriazeSpider;

    public static String redisIp;
    public static long redisPort;

    public static boolean jarMode;

    public static boolean noMethodCheckingException;

    public static boolean enableSwitchProxy;

    public static boolean enableRetrySpider;

    public static boolean enableScheduleImmediately;

    public static int jobNumToSleep;    //For @WorkingThread

    public static int jobSleepTimeToSleep;

    public static int jobSchedulerTimeMin;

    public static int jobSchedulerTimeMax;

    public static int considerBlockedBlockNum;

    public static int considerBlocked404Num;

    public static int considerBlockedMayblockNum;

    public static int considerBlockedNeterr;

    //从哪个路径下扫描checker 以';'隔开 如果没有这个值那么从跟路径开始扫描
    public static String redisSpiderCheckerBasePackage;

    public static long okhttpClientSocketReadTimeout;

    public static long shellCheckProxyFileSleepTime;

    public static int proxyHeartbeatInterval;

    public static boolean enableRuntimeInputProxy;

    public static boolean enableInitProxyFromFile;

    public static int everyProxyTryTime;

    public static boolean enableRadomInsertJob;

    public static boolean enableInsertDuplicateJob;

    public static long jobQueueEmptySleepTime;

    public final static String ipproxyFile = "/util/ipproxy.txt";

    public final static String fulllogFile = "/logs/htmls/";

    public final static String fulllogPost = ".html";

    public final static String serializedSpiderFile = "/file/spiders.txt";

    static {
        LogManager.info("Read Configuration From File:/conf/jobmanager.properties");
        readConfigFromFile();
    }

    private JobManagerConfig() {
        ;
    }

    private static void readConfigFromFile() {
        Properties pro = new Properties();
        FileInputStream in = null;
        boolean success = false;
        try {
            in = new FileInputStream(FileUtil.getCurrentPath()
                    + "/conf/jobmanager.properties");
            pro.load(in);
            success = true;
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            ;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    ;
                }

            }
        }

        if (!success) {
            pro = null; //确保一定会初始化
        }

        enableSeriazeSpider = parse(pro, "enableSeriazeSpider", false);

        newSpideMode = parse(pro, "newSpideMode", true);

        okhttpClientSocketReadTimeout = parse(pro, "okhttpClientSocketReadTimeout", (long) 10 * 1000);

        shellCheckProxyFileSleepTime = parse(pro, "shellCheckProxyFileSleepTime", (long) 1000 * 10);

        proxyHeartbeatInterval = parse(pro, "proxyHeartbeatInterval", 5 * 1000);


        enableSwitchProxy = parse(pro, "enableSwitchProxy", true);

        enableRuntimeInputProxy = parse(pro, "enableRuntimeInputProxy", true);

        enableInitProxyFromFile = parse(pro, "enableInitProxyFromFile", false);

        everyProxyTryTime = parse(pro, "everyProxyTryTime", 4);

        enableRadomInsertJob = parse(pro, "enableRadomInsertJob", false);

        enableInsertDuplicateJob = parse(pro, "enableInsertDuplicateJob", false);

        enableScheduleImmediately = parse(pro, "enableScheduleImmediately", false);

        jobQueueEmptySleepTime = parse(pro, "jobQueueEmptySleepTime", (long) 1000 * 10);

        jobNumToSleep = parse(pro, "jobNumToSleep", 10);

        jobSleepTimeToSleep = parse(pro, "jobSleepTimeToSleep", 1000 * 20);

        jobSchedulerTimeMin = parse(pro, "jobSchedulerTimeMin", 4);

        jobSchedulerTimeMax = parse(pro, "jobSchedulerTimeMax", 12);

        considerBlockedBlockNum = parse(pro, "considerBlockedBlockNum", 1);

        considerBlocked404Num = parse(pro, "considerBlocked404Num", 1);

        considerBlockedMayblockNum = parse(pro, "considerBlockedMayblockNum", 3);

        considerBlockedNeterr = parse(pro, "considerBlockedNeterr", 10);

        enableRetrySpider = parse(pro, "enableRetrySpider", true);


        redisIp = parse(pro, "redisIp", "127.0.0.1");
        redisPort = parse(pro, "redisPort", (long) 6379);

        noMethodCheckingException = parse(pro, "noMethodCheckingException", false);

        jarMode = parse(pro, "jarMode", true);

        distributeMode = parse(pro,"distributeMode",false);
        spiderIdentity = parse(pro,"spiderIdentity","none");

        //只有三种身份
        if(!spiderIdentity.equals("none")) {
            if(!spiderIdentity.equals("master") && !spiderIdentity.equals("agent")) {
                spiderIdentity = "none";
            }
        }

        if (distributeMode) {
            LogManager.info("Current SpiderMode: distributed");
        } else {
            LogManager.info("Current SpiderMode: single");
        }

        if (jarMode) {
            LogManager.info("Current RunningMode: jar");
        } else {
            LogManager.info("Current RunningMode: ide");
        }

    }

    private static String parse(@NotNull Properties pro, String key, String defValue) {
        if (pro == null) {
            return defValue;
        }

        try {
            return pro.getProperty(
                    key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }

    private static long parse(@Nullable Properties pro, String key, long defValue) {
        if (pro == null) {
            return defValue;
        }

        try {
            return Long.parseLong(pro.getProperty(
                    key, String.valueOf(defValue)));
        } catch (Exception e) {
            return defValue;
        }
    }

    private static int parse(@Nullable Properties pro, String key, int defValue) {
        if (pro == null) {
            return defValue;
        }

        try {
            return Integer.parseInt(pro.getProperty(
                    key, String.valueOf(defValue)));
        } catch (Exception e) {
            return defValue;
        }
    }

    private static boolean parse(@Nullable Properties pro, String key, boolean defValue) {
        if (pro == null) {
            return defValue;
        }

        try {
            return Boolean.parseBoolean(pro.getProperty(
                    key, String.valueOf(defValue)));
        } catch (Exception e) {
            return defValue;
        }
    }
}
