package wuxian.me.lagouspider.util;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.framework.BaseSpider;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 17/4/2017.
 */
public class LoggerSpider implements Runnable {

    private BaseSpider runnable;

    private LoggerSpider(BaseSpider runnable) {
        this.runnable = runnable;
    }

    public static LoggerSpider from(@NotNull BaseSpider real) {
        return new LoggerSpider(real);
    }

    public void run() {
        logger().info("Run spider: " + runnable.name());
        runnable.run();
    }
}
