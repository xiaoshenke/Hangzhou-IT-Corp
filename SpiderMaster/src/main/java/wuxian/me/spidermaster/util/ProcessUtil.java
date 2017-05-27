package wuxian.me.spidermaster.util;

import java.lang.management.ManagementFactory;

/**
 * Created by wuxian on 27/5/2017.
 */
public class ProcessUtil {

    private ProcessUtil() {
    }

    public static String getCurrentProcessId() {
        String name = ManagementFactory.getRuntimeMXBean().getName();

        return name.split("@")[0];
    }
}
