package wuxian.me.lagouspider.framework.log;

import com.sun.istack.internal.NotNull;

/**
 * Created by wuxian on 30/4/2017.
 */
public class LogManager {

    private static ILog logImpl = new DefaultLog();

    public static <T extends ILog> void setRealLogImpl(@NotNull T log) {
        logImpl = log;
    }

    public static void debug(String message) {
        logImpl.debug(message);
    }

    public static void info(String message) {
        logImpl.info(message);
    }

    public static void error(String message) {
        logImpl.info(message);
    }

    public static void warn(String message) {
        logImpl.warn(message);
    }
}
