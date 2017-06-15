package wuxian.me.lagouspider.util;

import com.sun.istack.internal.NotNull;
import okhttp3.Headers;
import wuxian.me.lagouspider.biz.boss.BossConfig;
import wuxian.me.lagouspider.biz.lagou.LagouConfig;
import wuxian.me.spidercommon.util.FileUtil;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.IJobManager;
import wuxian.me.spidersdk.anti.UserAgentManager;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.manager.JobManagerFactory;
import wuxian.me.spidersdk.util.CookieManager;

import java.text.SimpleDateFormat;
import java.util.*;

import static wuxian.me.lagouspider.biz.lagou.LagouConfig.File.CONF;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.Grab.CONF_LASTGRAB;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.Grab.GRAB_INTERNAL;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.TableName.*;
import static wuxian.me.spidercommon.util.FileUtil.getCurrentPath;

/**
 * Created by wuxian on 1/4/2017.
 */
public class Helper {

    public static String getBossDistinctsFilePath() {
        return getCurrentPath() + BossConfig.File.FILE + BossConfig.CITY_TO_SPIDER + BossConfig.File.DISTINTC;
    }

    public static String getLagouAreaFilePath() {
        return getCurrentPath() + LagouConfig.File.CONF + LagouConfig.CITY_TO_SPIDER + LagouConfig.File.AREA;
    }

    public static String getLagouDistinctsFilePath() {
        return getCurrentPath() + CONF + LagouConfig.CITY_TO_SPIDER + LagouConfig.File.DISTINTC;
    }

    public static String getLagouGrabFilePath() {
        return getCurrentPath() + CONF_LASTGRAB;
    }

    public static String getCookieFilePath(String spiderName) {
        return getCurrentPath() + Config.File.CONF_COOKIE + spiderName;
    }

    public static String getLog4jPropFilePath() {
        return getCurrentPath() + LagouConfig.File.CONF_LOG4J_PROPERTIES;
    }

    public static String getWriteLogFilePath() {
        return getCurrentPath() + "/logs/error.log";
    }

    //每个城市一张表
    public static String getLagouCompanyTableName() {
        return LagouConfig.CITY_TO_SPIDER + TABLE_COMPANY + getDatabasePost();
    }

    public static String getLagouProductTableName() {
        return LagouConfig.CITY_TO_SPIDER + TABLE_PRODUCT + getDatabasePost();
    }

    public static String getLagouLocationTableName() {
        return LagouConfig.CITY_TO_SPIDER + TABLE_LOCATION + getDatabasePost();
    }

    public static String getLagouAreaTableName() {
        return LagouConfig.CITY_TO_SPIDER + TABLE_AREA;
    }

    private Helper() {
    }

    private static String post = null;
    private static final String HEADER_REFERER = "Referer";
    private static Headers.Builder builder;

    static {
        builder = new Headers.Builder();
        builder.add("Cookie", "");
        builder.add("Connection", "keep_alive");
        builder.add("Host", "www.lagou.com");
        builder.add(HEADER_REFERER, "abd");
        builder.add("User-Agent", "ab");
    }

    public static Headers getHeaderBySpecifyRef(@NotNull String reference, @NotNull String spiderName) {
        if (!CookieManager.containsKey(spiderName)) {
            if (FileUtil.checkFileExist(getCookieFilePath(spiderName))) {
                String content = FileUtil.readFromFile(getCookieFilePath(spiderName));
                if (content != null && content.length() != 0) {
                    CookieManager.put(spiderName, content);
                }
            }
        }

        builder.set("Cookie", CookieManager.get(spiderName));
        builder.set(HEADER_REFERER, reference);
        builder.set("User-Agent", UserAgentManager.getAgent());
        return builder.build();
    }

    public static Headers getBossHeader(@NotNull String reference, @NotNull String spiderName) {
        builder.set("Host", BossConfig.HOST);
        return getHeaderBySpecifyRef(reference, spiderName);
    }

    public static Headers getLagouHeader(@NotNull String reference, @NotNull String spiderName) {
        builder.set("Host", LagouConfig.HOST);
        return getHeaderBySpecifyRef(reference, spiderName);
    }

    private static String getDatabasePost() {
        if (post != null) {
            return post;
        }
        String content = FileUtil.readFromFile(getLagouGrabFilePath());
        long time = Long.parseLong(content);

        SimpleDateFormat dd = new SimpleDateFormat("yyyy_MM_dd");
        return post = "_" + dd.format(new Date(time));
    }

    public static boolean shouldStartNewGrab() {
        if (!FileUtil.checkFileExist(getLagouGrabFilePath())) {
            return true;
        }
        String content = FileUtil.readFromFile(getLagouGrabFilePath());

        if (content == null) {
            return true;
        }
        long time = Long.parseLong(content);
        return (System.currentTimeMillis() - time > GRAB_INTERNAL);
    }

    public static void updateNewGrab() {
        FileUtil.writeToFile(getLagouGrabFilePath(), String.valueOf(System.currentTimeMillis()));
    }

    private static IJobManager jobManager;

    static {
        jobManager = JobManagerFactory.getJobManager();
    }

    public static void dispatchSpider(@NotNull BaseSpider spider) {
        IJob job = JobProvider.getJob();
        job.setRealRunnable(spider);

        jobManager.putJob(job);
    }

    public static void dispatchSpider(@NotNull BaseSpider spider, boolean force) {
        IJob job = JobProvider.getJob();
        job.setRealRunnable(spider);

        jobManager.putJob(job, force);
    }

}
