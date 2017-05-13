package wuxian.me.lagouspider.util;

import com.sun.istack.internal.NotNull;
import okhttp3.Headers;
import wuxian.me.lagouspider.biz.boss.BossConfig;
import wuxian.me.lagouspider.biz.lagou.LagouConfig;
import wuxian.me.lagouspider.biz.tianyancha.TianyanConfig;
import wuxian.me.lagouspider.biz.zhishu.ZhishuConfig;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.JobManager;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.util.FileUtil;

import java.text.SimpleDateFormat;
import java.util.*;

import static wuxian.me.lagouspider.biz.lagou.LagouConfig.*;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.File.*;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.Grab.CONF_LASTGRAB;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.Grab.GRAB_INTERNAL;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.TableName.*;
import static wuxian.me.spidersdk.util.FileUtil.getCurrentPath;

/**
 * Created by wuxian on 1/4/2017.
 */
public class Helper {

    public static String getBossDistinctsFilePath() {
        return getCurrentPath() + BossConfig.File.CONF + BossConfig.CITY_TO_SPIDER + BossConfig.File.DISTINTC;
    }

    public static String getLagouAreaFilePath() {
        return getCurrentPath() + CONF + LagouConfig.CITY_TO_SPIDER + AREA;
    }

    public static String getLagouDistinctsFilePath() {
        return getCurrentPath() + CONF + CITY_TO_SPIDER + DISTINTC;
    }

    public static String getLagouGrabFilePath() {
        return getCurrentPath() + CONF_LASTGRAB;
    }

    public static String getLagouCookieFilePath(String spiderName) {
        return getCurrentPath() + CONF_COOKIE + "_" + spiderName;
    }

    public static String getLog4jPropFilePath() {
        return getCurrentPath() + CONF_LOG4J_PROPERTIES;
    }

    //每个城市一张表
    public static String getLagouCompanyTableName() {
        return CITY_TO_SPIDER + TABLE_COMPANY + getDatabasePost();
    }

    public static String getLagouProductTableName() {
        return CITY_TO_SPIDER + TABLE_PRODUCT + getDatabasePost();
    }

    public static String getLagouLocationTableName() {
        return CITY_TO_SPIDER + TABLE_LOCATION + getDatabasePost();
    }

    public static String getLagouAreaTableName() {
        return CITY_TO_SPIDER + TABLE_AREA;
    }

    private Helper() {
    }

    private static String post = null;
    private static final String HEADER_REFERER = "Referer";
    private static Headers.Builder builder;

    private static Map<String, String> cookieList = new HashMap<String, String>();

    static {
        builder = new Headers.Builder();
        builder.add("Cookie", "");
        builder.add("Connection", "keep_alive");
        builder.add("Host", "www.lagou.com");
        builder.add(HEADER_REFERER, "abd");
        builder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
    }

    public static Headers getHeaderBySpecifyRef(@NotNull String reference, @NotNull String spiderName) {
        if (!cookieList.containsKey(spiderName)) {
            if (FileUtil.checkFileExist(getLagouCookieFilePath(spiderName))) {
                String content = FileUtil.readFromFile(getLagouCookieFilePath(spiderName));
                if (content != null && content.length() != 0) {
                    cookieList.put(spiderName, content);
                }
            }
        }

        builder.set("Cookie", cookieList.get(spiderName));
        builder.set(HEADER_REFERER, reference);
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

    public static Headers getTianyanHeader(@NotNull String reference, @NotNull String spiderName) {
        builder.set("Host", TianyanConfig.HOST);
        return getHeaderBySpecifyRef("", spiderName);
    }

    public static Headers getZhishuHeader(@NotNull String reference, @NotNull String spiderName) {
        builder.set("Host", ZhishuConfig.HOST);
        return getHeaderBySpecifyRef(reference, spiderName);
    }


    /**
     * 用于数据库"根据不同时间"分表:分表的目的用于后续研究公司变迁的数据：比如说某块区域的公司迁移数据 某个公司的招聘岗位的变化等
     * <p>
     * 找到数据库后缀
     * 数据库每过1周会全新抓取一次拉勾数据 因此要做分表操作
     * 表的名字像这样 company_2017_0303,company_2017_0403
     */
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

    private static JobManager jobManager;

    static {
        jobManager = JobManager.getInstance();
    }

    public static void dispatchSpider(@NotNull BaseSpider spider) {
        IJob job = JobProvider.getJob();
        job.setRealRunnable(spider);

        jobManager.putJob(job);
    }

}
