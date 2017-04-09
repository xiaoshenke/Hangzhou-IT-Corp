package wuxian.me.lagouspider;

/**
 * Created by wuxian on 1/4/2017.
 */
public class Config {

    public static boolean IS_TEST = true;

    public final static int FAIL_FREQUENCY_NETWORK_ERR = 5;
    public final static int FAIL_FREQUENCY_SERVER_ERR = 5;

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

    public interface DB {

        String TABLE_DISTINCT_AREA = "area";  //杭州某个区下对应的街道

        String TABLE_AREA_COMPANY = "company";   //某个街道里的company
    }

}
