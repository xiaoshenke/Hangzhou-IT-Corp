package wuxian.me.lagouspider.framework.log;

/**
 * Created by wuxian on 30/4/2017.
 */
public class DefaultLog implements ILog {

    public void debug(String message) {
        System.out.println(message);
    }

    public void info(String message) {
        System.out.println(message);
    }

    public void error(String message) {
        System.out.println(message);
    }

    public void warn(String message) {
        System.out.println(message);
    }
}
