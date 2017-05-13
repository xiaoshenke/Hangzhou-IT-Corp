package wuxian.me.spidersdk.distribute;

import wuxian.me.spidersdk.BaseSpider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by wuxian on 12/5/2017.
 *
 * Runtime检查@BaseSpider的子类是否实现了@BaseSpider.fromUrlNode,toUrlNode,若没有,抛异常
 */
public class RedisSpiderChecker {

    private RedisSpiderChecker() {
    }

    public static void performCheck(String pack) throws MethodCheckException {
        try {
            Set<Class<?>> classSet = ClassFileUtil.getClasses(pack);

            for (Class clazz : classSet) {
                performCheck(clazz);
            }
        } catch (IOException e) {
            ;
        }
    }

    public static void performCheck(Class clazz) throws MethodCheckException {
        try {
            clazz.asSubclass(BaseSpider.class);

            Method method = clazz.getMethod("fromUrlNode", HttpUrlNode.class);
            if (!(method.getClass().getSimpleName().equals(clazz.getSimpleName()))) {
                throw new MethodCheckException();
            }

            method = clazz.getMethod("toUrlNode", Void.class);
            if (!(method.getClass().getSimpleName().equals(clazz.getSimpleName()))) {
                throw new MethodCheckException();
            }
        } catch (NoSuchMethodException e) {
            //Todo:

        } catch (ClassCastException e) {

        }
    }
}
