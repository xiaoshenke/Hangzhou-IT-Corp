package wuxian.me.lagouspider;

/**
 * Created by wuxian on 1/4/2017.
 */
public class Config {

    public static boolean IS_TEST = true;

    public static int SOCKET_READ_TIMEOUT = 10 * 1000; //10s

    public static boolean ENABLE_SAVE_COMPANY_DB = false;

    public static boolean ENABLE_RETRY_SPIDER = false;

    public static boolean ENABLE_SWITCH_IPPROXY = false;

    public static int MAX_FAIL_TIME = 4;

    public static final String URL_LAGOU_COMPANY_MAIN = "https://www.lagou.com/gongsi/";

    public static final String URL_LAGOU_POSITION_JSON = "https://www.lagou.com/jobs/positionAjax.json?px=default";

    public static final String URL_LAGOU_JAVA = "https://www.lagou.com/jobs/list_Java?px=default";

    public static final String TABLE_COMPANY = "companies";

    //7天全面抓一次
    public static final long GRAB_INTERNAL = 1000 * 60 * 60 * 24 * 7;
    //上一次爬虫抓取的时间
    public static final String CONF_LASTGRAB = "/conf/lastgrab.txt";

    //存储区域的文件
    public static final String CONF_DISTINTC = "/conf/distinct.txt";

    //存储街道的文件
    public static final String CONF_AREA = "/conf/area.txt";

    public static final String CONF_COOKIE = "/conf/cookies";

    public static final String CONF_LOG4J_PROPERTIES = "/log4j.properties";

    public static final String CONF_FULLLOG_TEXT = "/htmls/";

    public static final String CONF_FULLLOG_TEXT_POST = ".txt";

}
