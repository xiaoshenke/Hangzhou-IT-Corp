package wuxian.me.spidersdk.log;

import com.sun.istack.internal.NotNull;

/**
 * Created by wuxian on 30/4/2017.
 * <p>
 * 日志级别规定:
 * 1 监控整个项目运行的info级别 比如切换ip,job状态切换:开始运行,成功,失败,重试等
 * 2 Job出错的error级
 * 3 其他debug级别 比如parsing什么的
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
        logImpl.error(message);
    }

    public static void warn(String message) {
        logImpl.warn(message);
    }
}
