package wuxian.me.spidersdk;

import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.distribute.SpiderClassChecker;
import wuxian.me.spidersdk.distribute.SpiderMethodTuple;
import wuxian.me.spidersdk.util.FileUtil;

import java.io.File;

/**
 * Created by wuxian on 12/5/2017.
 */
public class Main {

    static {
        File file = null;
        try {
            file = new File(FileUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            file = file.getParentFile();
            FileUtil.setCurrentPath(file.getAbsolutePath());
        } catch (Exception e) {

        }

    }

    public static void main(String[] args) {
        NoneSpider spider = new NoneSpider();
        System.out.println("Hello World,This Is Main.main");
        System.out.println("redis enabled: " + JobManagerConfig.useRedisJobQueue);

        try {
            Class clazz = ClassHelper.getClassByName("wuxian.me.spidersdk.NoneSpider");
            SpiderMethodTuple tuple = SpiderClassChecker.performCheckAndCollect(clazz);
            System.out.println("success");
        } catch (ClassNotFoundException e) {

        }

        JobManager.getInstance(); //会进行BaseSpider check
    }
}
