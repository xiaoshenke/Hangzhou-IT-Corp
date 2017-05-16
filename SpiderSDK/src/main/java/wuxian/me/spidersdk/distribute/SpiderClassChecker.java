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
public class SpiderClassChecker {

    private SpiderClassChecker() {
    }

    public static void performCheckAndCollect(String pack) throws MethodCheckException {
        try {
            Set<Class<?>> classSet = ClassHelper.getClasses(pack);

            for (Class clazz : classSet) {
                performCheckAndCollect(clazz);
            }
        } catch (IOException e) {
            ;
        }
    }

    public static SpiderMethodTuple performCheckAndCollect(Class clazz) throws MethodCheckException {
        SpiderMethodTuple ret = null;
        try {
            clazz.asSubclass(BaseSpider.class);

            Method method1 = clazz.getMethod("toUrlNode", clazz);
            if (!(method1.getDeclaringClass().getSimpleName().equals(clazz.getSimpleName()))) {
                throw new MethodCheckException();
            }

            Method method = clazz.getMethod("fromUrlNode", HttpUrlNode.class);

            if (!(method.getDeclaringClass().getSimpleName().equals(clazz.getSimpleName()))) {
                throw new MethodCheckException();
            }

            ret = new SpiderMethodTuple();
            ret.fromUrlNode = method;
            ret.toUrlNode = method1;
        } catch (NoSuchMethodException e) {
            //Todo:

        } catch (ClassCastException e) {

        }

        return ret;
    }
}
