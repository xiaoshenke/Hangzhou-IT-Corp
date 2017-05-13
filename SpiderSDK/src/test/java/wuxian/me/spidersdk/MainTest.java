package wuxian.me.spidersdk;

import org.junit.Test;
import wuxian.me.spidersdk.distribute.ClassFileUtil;
import wuxian.me.spidersdk.distribute.RedisSpiderChecker;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by wuxian on 12/5/2017.
 */
public class MainTest {

    @Test
    public void testClassFileUtil() {
        try {
            Set<Class<?>> classSet = ClassFileUtil.getClasses("wuxian.me.spidersdk.log");

            for (Class c : classSet) {
                System.out.println(c.getName());
            }
        } catch (IOException e) {

        }
    }

    @Test
    public void testStaticMethodCheck() {
        try {
            RedisSpiderChecker.performCheck(ClassFileUtil.getClassByName("wuxian.me.spidersdk.NoneSpider"));

            System.out.println("success");
        } catch (Exception e) {

        }
    }
}