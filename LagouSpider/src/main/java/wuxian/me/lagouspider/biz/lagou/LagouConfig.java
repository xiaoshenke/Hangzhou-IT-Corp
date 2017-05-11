package wuxian.me.lagouspider.biz.lagou;

/**
 * Created by wuxian on 1/4/2017.
 * <p>
 *
 */
public interface LagouConfig {

    String SPIDER_NAME = "LagouSpider";

    String CITY_TO_SPIDER = "杭州";

    String CUT = ";";
    String SEPRATE = ":";
    String HOST = "www.lagou.com";

    int SHORTNAME_MAX = 6;
    int FULLNAME_MAX = 15;
    int WEBLINK_MAX = 41;

    interface SaveDBThread {

        //每1分钟存储一次数据库
        int SAVE_COMPANY_INTERVAL = 1000 * 30 * 1;

        int SAVE_COMPANY_MAIN_INTERVAL = (int) (1000 * 40 * 1);

        int SAVE_PRODUCT_INTERVAL = 1000 * 60 * 1;

        int SAVE_LOCATION_INTERVAL = 1000 * 60 * 1;
    }

    interface EnableSaveDB {

        boolean ENABLE_SAVE_PRODUCT_DB = false;

        boolean ENABLE_SAVE_LOCATION_DB = false;

        boolean ENABLE_SAVE_COMPANY_DB = true;
    }

    interface Spider {

        boolean ENABLE_SPIDER_DISTINCT = true;

        boolean ENABLE_SPIDER_AREAPAGE = true;

        boolean ENABLE_SPIDER_COMPANY_MAIN = false;

        boolean ENABLE_SPIDER_ITCHENGZI_SEARCH = false;
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

        String TABLE_AREA = "area";
    }

    interface Location {

        String LOCATION_MULTY = "multi";

        String LOCATION_NONE = "none";

        String LOCATION_SINGLE = "single";
    }

    interface Grab {

        //7天全面抓一次
        long GRAB_INTERNAL = 1000 * 60 * 60 * 24 * 7;

        //上一次爬虫抓取的时间
        String CONF_LASTGRAB = "/conf/lastgrab.txt";
    }

    interface File {

        String CONF = "/conf/";

        //存储区域的文件
        String DISTINTC = "distinct.txt";

        //存储街道的文件
        String AREA = "area.txt";

        String CONF_COOKIE = "/conf/cookies";

        String CONF_LOG4J_PROPERTIES = "/log4j.properties";

    }

}
