package wuxian.me.lagouspider;

/**
 * Created by wuxian on 1/4/2017.
 */
public class Config {

    public static boolean IS_TEST = true;

    public static long FIXED_DELAYJOB_INTERVAL = 500;

    public static int SOCKET_READ_TIMEOUT = 10 * 1000; //10s

    public static boolean ENABLE_SPIDER_AREAPAGE = true;

    public static boolean ENABLE_PRINT_PARSED_COMPANY = false;

    public static boolean ENABLE_SAVE_PRODUCT_DB = false;

    public static boolean ENABLE_SAVE_LOCATION_DB = false;

    public static boolean ENABLE_SAVE_COMPANY_DB = true;

    public static boolean ENABLE_SPIDER_COMPANY_MAIN = true;

    public static boolean ENABLE_SPIDER_ITCHENGZI_SEARCH = false;

    public static boolean ENABLE_RETRY_SPIDER = false;

    public static boolean ENABLE_SWITCH_IPPROXY = false;

    public static int SINGLEJOB_MAX_FAIL_TIME = 4;  //单个job的最大失败次数

    public static final String URL_LAGOU_COMPANY_MAIN = "https://www.lagou.com/gongsi/";

    public static final String URL_LAGOU_POSITION_JSON = "https://www.lagou.com/jobs/positionAjax.json?px=default";

    public static final String URL_LAGOU_JAVA = "https://www.lagou.com/jobs/list_Java?px=default";

    public static final String TABLE_COMPANY = "companies";

    public static final String TABLE_PRODUCT = "products";

    public static final String TABLE_LOCATION = "locations";

    public static String LOCATION_MULTY = "multi";
    public static String LOCATION_NONE = "none";

    //每1分钟存储一次数据库

    public static int SAVE_COMPANY_INTERVAL = 1000 * 1 * 5;

    public static int SAVE_COMPANY_MAIN_INTERVAL = (int) (1000 * 5 * 1);

    public static int SAVE_PRODUCT_INTERVAL = 1000 * 1 * 5;

    public static int SAVE_LOCATION_INTERVAL = 1000 * 1 * 5;

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

    public static final String CONF_FULLLOG_TEXT_POST = ".html";

}
