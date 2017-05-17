package wuxian.me.spidersdk;

import org.junit.Test;
import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.distribute.SpiderClassChecker;
import wuxian.me.spidersdk.distribute.SpiderMethodTuple;
import wuxian.me.spidersdk.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by wuxian on 12/5/2017.
 */
public class MainTest {

    //Todo
    @Test
    public void testRedisJobQueue() {

        //Class clazz = new NoneSpider().getClass();
        //SpiderMethodTuple tuple = SpiderClassChecker.performCheckAndCollect(clazz);
        SpiderClassChecker.performCheckAndCollect(NoneSpider.class);
    }

    @Test
    public void testPath() {
        System.out.println(FileUtil.getCurrentPath());
    }

    @Test
    public void testClass() {

        File file = null;
        String jarPath = "";
        try {
            jarPath = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            file = new File(jarPath);
            file = file.getParentFile();
        } catch (Exception e) {
        }
        FileUtil.setCurrentPath(file.getAbsolutePath());

        System.out.println(jarPath);
        //NoneSpider spider = new NoneSpider();
        //spider.toUrlNode();
    }

    @Test
    public void testClassFileUtil() {
        try {
            Set<Class<?>> classSet = ClassHelper.getClasses("wuxian.me.spidersdk.log");

            for (Class c : classSet) {
                System.out.println(c.getName());
            }
        } catch (IOException e) {

        }
    }

    @Test
    public void testStaticMethodCheck() {
        try {
            SpiderClassChecker.performCheckAndCollect(ClassHelper.getClassByName("wuxian.me.spidersdk.NoneSpider"));

            System.out.println("success");
        } catch (Exception e) {

        }
    }
}