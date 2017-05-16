package wuxian.me.spidersdk.distribute;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 15/5/2017.
 */
public class SpiderMethodManager {

    private SpiderMethodManager() {
    }

    private static Map<Class, SpiderMethodTuple> spiderClassMap = new HashMap<Class, SpiderMethodTuple>();

    private static Map<String, Class> classMap = new ConcurrentHashMap<String, Class>();

    private static boolean contains(@NotNull Class clazz) {
        return spiderClassMap.containsKey(clazz);
    }

    public static boolean contains(@NotNull String name) {
        if (!classMap.containsKey(name)) {
            return false;
        }

        return contains(classMap.get(name));
    }

    public static void put(@NotNull Class clazz, @NotNull SpiderMethodTuple tuple) {
        spiderClassMap.put(clazz, tuple);
    }

    @Nullable
    public static Method getToUrlMethod(@NotNull String name) {
        if (!contains(name)) {
            return null;
        }
        return getToUrlMethod(classMap.get(name));
    }

    public static Method getToUrlMethod(@NotNull Class clazz) {
        if (!spiderClassMap.containsKey(clazz)) {
            return null;
        }

        return spiderClassMap.get(clazz).toUrlNode;
    }

    public static Method getFromUrlMethod(@NotNull Class clazz) {
        if (!spiderClassMap.containsKey(clazz)) {
            return null;
        }

        return spiderClassMap.get(clazz).fromUrlNode;
    }

    public static Set<Class> getSpiderClasses() {
        return spiderClassMap.keySet();
    }

}
