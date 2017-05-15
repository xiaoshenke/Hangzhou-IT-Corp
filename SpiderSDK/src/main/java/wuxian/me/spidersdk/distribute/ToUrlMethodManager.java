package wuxian.me.spidersdk.distribute;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 15/5/2017.
 */
public class ToUrlMethodManager {

    private ToUrlMethodManager() {
    }

    private static Map<Class, Method> methodMap = new ConcurrentHashMap<Class, Method>();

    private static Map<String, Class> classMap = new ConcurrentHashMap<String, Class>();

    private static boolean contains(@NotNull Class clazz) {
        return methodMap.containsKey(clazz);
    }

    public static boolean contains(@NotNull String name) {
        if (!classMap.containsKey(name)) {
            return false;
        }

        return contains(classMap.get(name));
    }

    @Nullable
    public static Method getMethod(@NotNull String name) {
        if (!contains(name)) {
            return null;
        }

        return getMethod(classMap.get(name));
    }

    private static Method getMethod(@NotNull Class clazz) {
        return methodMap.get(clazz);
    }

    public static void put(@NotNull Class clazz, @NotNull Method method) {
        methodMap.put(clazz, method);
        classMap.put(clazz.getName(), clazz);
    }

}
