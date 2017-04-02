package wuxian.me.lagouspider.util;

import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.util.FileUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wuxian on 1/4/2017.
 */
public class Helper {
    private Helper() {
    }

    public static boolean isTest = true;

    private static String post = null;

    /**
     * 用于数据库"根据不同时间"分表:分表的目的用于后续研究公司变迁的数据：比如说某块区域的公司迁移数据 某个公司的招聘岗位的变化等
     * <p>
     * 找到数据库后缀
     * 数据库每过1周会全新抓取一次拉勾数据 因此要做分表操作
     * 表的名字像这样 company_2017_0303,company_2017_0403
     *
     * @return
     */
    public static String getDatabasePost() {
        if (post != null) {
            return post;
        }
        String content = FileUtil.readFromFile(FileUtil.getGrabFilePath());
        long time = Long.parseLong(content);

        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        return post = "_" + dd.format(new Date(time));
    }

    public static boolean shouldStartNewGrab() {
        if (!FileUtil.checkFileExist(FileUtil.getGrabFilePath())) {
            return true;
        }
        String content = FileUtil.readFromFile(FileUtil.getGrabFilePath());

        if (content == null) {
            return true;  //Fixme
        }
        long time = Long.parseLong(content);
        return (System.currentTimeMillis() - time > Config.GRAB_INTERNAL);
    }

    public static void updateNewGrab() {
        FileUtil.writeToFile(FileUtil.getGrabFilePath(), String.valueOf(System.currentTimeMillis()));
    }
}
