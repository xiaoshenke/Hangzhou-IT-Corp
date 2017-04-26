package wuxian.me.lagouspider;

/**
 * Created by wuxian on 1/4/2017.
 * <p>
 */
public interface Config {

    boolean IS_TEST = true;

    int OKHTTPCLIENT_SOCKET_READ_TIMEOUT = 10 * 1000; //10s

    int SWITCH_AGENT_NUM = 3; //每3个请求更换一次agent_

    interface Shell {
        String OPENPROXY = "/shell/openproxy";

        String CHECK_PROCESS_EXSIT = "/shell/processexist";

        long SLEEP_TIME_CHECK_PROXY_INPUTED = 1000 * 10;
    }

    interface ProxyControl {

        int PROXY_HEARTBEAT_FREQUENCY = 5 * 1000; //proxy的心跳频率 暂定5秒好了

        boolean ENABLE_SWITCH_IPPROXY = true;

        boolean ENABLE_RUNTIME_INPUT_PROXY = true;

        boolean ENABLE_READ_PROXY_FROM_FILE = true;

    }

    interface SaveDBThread {

        //每1分钟存储一次数据库
        int SAVE_COMPANY_INTERVAL = 1000 * 60 * 1;

        int SAVE_COMPANY_MAIN_INTERVAL = (int) (1000 * 60 * 1.5);

        int SAVE_PRODUCT_INTERVAL = 1000 * 60 * 1;

        int SAVE_LOCATION_INTERVAL = 1000 * 60 * 1;
    }

    interface EnableSaveDB {

        boolean ENABLE_SAVE_PRODUCT_DB = true;

        boolean ENABLE_SAVE_LOCATION_DB = true;

        boolean ENABLE_SAVE_COMPANY_DB = true;
    }

    interface Queue {
        boolean ENABLE_RANDOM_INSERT = true;

        boolean ENABLE_DUPLICATE_INSERT = false;
    }

    interface JobScheduler {

        boolean SCHEDULE_IMMEDIATELY = false;

        long SLEEP_WHEN_QUEUE_EMPTY = 1000 * 10;

        long SWITCH_SLEEP_JOB_NUMBER = 10;  //每x个任务休息

        long SWITCH_SLEEP_SLEEP_TIME = 1000 * 30; //每x个任务休息xs

        int SLEEP_TIME_MIN = 8;        //每个任务最小相隔xs

        int SLEEP_TIME_MAX = 22;        //每个任务最大相隔xs

    }

    interface Spider {

        boolean ENABLE_SPIDER_AREAPAGE = true;

        boolean ENABLE_SPIDER_COMPANY_MAIN = true;

        boolean ENABLE_SPIDER_ITCHENGZI_SEARCH = false;
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

        String CONF_IPPROXY = "/conf/ipproxy.txt";
    }


    interface JobProvider {

        boolean USE_FIXED_DELAY_NEXT_JOB = true;

        boolean USE_FIXED_DELAY_JOB = true;

        long FIXED_DELAYJOB_INTERVAL = 1000 * 4;  //3秒一个request
    }
}
