package wuxian.me.spidercommon.util;

import java.lang.management.ManagementFactory;

/**
 * Created by wuxian on 11/6/2017.
 */
public class ProcessUtil {

    private ProcessUtil() {
    }

    public static String getCurrentProcessId() {
        String name = ManagementFactory.getRuntimeMXBean().getName();

        return name.split("@")[0];
    }
}
