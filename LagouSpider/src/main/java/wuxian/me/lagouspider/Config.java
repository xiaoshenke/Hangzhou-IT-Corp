package wuxian.me.lagouspider;

/**
 * Created by wuxian on 1/4/2017.
 * <p>
 * Todo:重构 不要用class
 */
public interface Config {

    boolean IS_TEST = true;

    int OKHTTPCLIENT_SOCKET_READ_TIMEOUT = 10 * 1000; //10s

    interface ProxyControl {

        int PROXY_HEARTBEAT_FREQUENCY = 5 * 1000; //暂定5秒好了

        boolean ENABLE_SWITCH_IPPROXY = true;

    }

    interface JobProvider {

        boolean USE_FIXED_DELAY_NEXT_JOB = true;

        boolean USE_FIXED_DELAY_JOB = true;

        long FIXED_DELAYJOB_INTERVAL = 100;
    }


    interface Spider {

        boolean ENABLE_SPIDER_AREAPAGE = true;

        boolean ENABLE_SPIDER_COMPANY_MAIN = true;

        boolean ENABLE_SPIDER_ITCHENGZI_SEARCH = false;
    }

    interface EnableSaveDB {

        boolean ENABLE_SAVE_PRODUCT_DB = true;

        boolean ENABLE_SAVE_LOCATION_DB = true;

        boolean ENABLE_SAVE_COMPANY_DB = true;
    }

    interface RetryControl {

        boolean ENABLE_RETRY_SPIDER = true;

        int SINGLEJOB_MAX_FAIL_TIME = 4;  //单个job的最大失败次数

    }

    interface SpiderUrl {

        String URL_LAGOU_COMPANY_MAIN = "https://www.lagou.com/gongsi/";

        String URL_LAGOU_POSITION_JSON = "https://www.lagou.com/jobs/positionAjax.json?px=default";

        String URL_LAGOU_JAVA = "https://www.lagou.com/jobs/list_Java?px=default";
    }

    interface TableName {

        String TABLE_COMPANY = "companies";

        String TABLE_PRODUCT = "products";

        String TABLE_LOCATION = "locations";
    }

    interface Location {

        String LOCATION_MULTY = "multi";

        String LOCATION_NONE = "none";
    }

    interface SaveDBThread {

        //每1分钟存储一次数据库
        int SAVE_COMPANY_INTERVAL = 1000 * 1 * 5;

        int SAVE_COMPANY_MAIN_INTERVAL = (int) (1000 * 5 * 1);

        int SAVE_PRODUCT_INTERVAL = 1000 * 1 * 5;

        int SAVE_LOCATION_INTERVAL = 1000 * 1 * 5;
    }


    interface Grab {

        //7天全面抓一次
        long GRAB_INTERNAL = 1000 * 60 * 60 * 24 * 7;

        //上一次爬虫抓取的时间
        String CONF_LASTGRAB = "/conf/lastgrab.txt";
    }

    interface FullLog {
        String CONF_FULLLOG_TEXT = "/htmls/";

        String CONF_FULLLOG_TEXT_POST = ".html";

    }

    interface File {
        //存储区域的文件
        String CONF_DISTINTC = "/conf/distinct.txt";

        //存储街道的文件
        String CONF_AREA = "/conf/area.txt";

        String CONF_COOKIE = "/conf/cookies";

        String CONF_LOG4J_PROPERTIES = "/log4j.properties";
    }

}
