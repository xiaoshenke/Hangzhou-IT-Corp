package wuxian.me.lagouspider.util;

import com.sun.istack.internal.NotNull;
import okhttp3.Headers;
import wuxian.me.lagouspider.framework.FileUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import static wuxian.me.lagouspider.Config.*;
import static wuxian.me.lagouspider.Config.File.*;
import static wuxian.me.lagouspider.Config.FullLog.CONF_FULLLOG_TEXT;
import static wuxian.me.lagouspider.Config.FullLog.CONF_FULLLOG_TEXT_POST;
import static wuxian.me.lagouspider.Config.Grab.CONF_LASTGRAB;
import static wuxian.me.lagouspider.Config.Grab.GRAB_INTERNAL;
import static wuxian.me.lagouspider.Config.TableName.*;
import static wuxian.me.lagouspider.framework.FileUtil.getCurrentPath;

/**
 * Created by wuxian on 1/4/2017.
 */
public class Helper {

    public static String getFullLogFilePath(String filename) {
        return getCurrentPath() + CONF_FULLLOG_TEXT + filename + CONF_FULLLOG_TEXT_POST;
    }

    public static String getAreaFilePath() {
        return getCurrentPath() + CONF_AREA;
    }

    public static String getDistinctsFilePath() {
        return getCurrentPath() + CONF_DISTINTC;
    }

    public static String getGrabFilePath() {
        return getCurrentPath() + CONF_LASTGRAB;
    }

    public static String getCookieFilePath() {
        return getCurrentPath() + CONF_COOKIE;
    }

    public static String getLog4jPropFilePath() {
        return getCurrentPath() + CONF_LOG4J_PROPERTIES;
    }


    private Helper() {
    }

    private static String post = null;
    private static final String HEADER_REFERER = "Referer";
    private static Headers.Builder builder;

    static {
        builder = new Headers.Builder();
        builder.add("Connection", "keep_alive");
        builder.add("Host", "www.lagou.com");
        builder.add(HEADER_REFERER, "abd");
        builder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
    }

    private static boolean cookieInit = false;

    public static Headers getHeaderBySpecifyRef(@NotNull String reference) {
        if (!cookieInit) {
            if (FileUtil.checkFileExist(getCookieFilePath())) {
                String content = FileUtil.readFromFile(getCookieFilePath());
                if (content != null && content.length() != 0) {
                    builder.add("Cookie", content);
                    cookieInit = true;
                }
            }
        }

        builder.set(HEADER_REFERER, reference);
        return builder.build();
    }

    public static String getCompanyTableName() {
        return TABLE_COMPANY + getDatabasePost();
    }

    public static String getProductTableName() {
        return TABLE_PRODUCT + getDatabasePost();
    }

    public static String getLocationTableName() {
        return TABLE_LOCATION + getDatabasePost();
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
        String content = FileUtil.readFromFile(getGrabFilePath());
        long time = Long.parseLong(content);

        SimpleDateFormat dd = new SimpleDateFormat("yyyy_MM_dd");
        return post = "_" + dd.format(new Date(time));
    }

    public static boolean shouldStartNewGrab() {
        if (!FileUtil.checkFileExist(getGrabFilePath())) {
            return true;
        }
        String content = FileUtil.readFromFile(getGrabFilePath());

        if (content == null) {
            return true;
        }
        long time = Long.parseLong(content);
        return (System.currentTimeMillis() - time > GRAB_INTERNAL);
    }

    public static void updateNewGrab() {
        FileUtil.writeToFile(getGrabFilePath(), String.valueOf(System.currentTimeMillis()));
    }

}
