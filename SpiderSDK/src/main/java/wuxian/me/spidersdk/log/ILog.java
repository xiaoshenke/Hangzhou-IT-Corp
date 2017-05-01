package wuxian.me.spidersdk.log;

/**
 * Created by wuxian on 30/4/2017.
 */
public interface ILog {

    void debug(String message);

    void info(String message);

    void error(String message);

    void warn(String message);
}
