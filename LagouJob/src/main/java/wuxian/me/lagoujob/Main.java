package wuxian.me.lagoujob;

import static wuxian.me.lagoujob.FileUtil.getCurrentPath;

/**
 * Created by wuxian on 1/5/2017.
 */
public class Main {

    static String CONF_LOG4J_PROPERTIES = "/log4j.properties";

    static {
        //PropertyConfigurator.configure(getLog4jPropFilePath());
    }

    public static String getLog4jPropFilePath() {
        return getCurrentPath() + CONF_LOG4J_PROPERTIES;
    }
}
