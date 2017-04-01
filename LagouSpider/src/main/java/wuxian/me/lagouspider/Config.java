package wuxian.me.lagouspider;

/**
 * Created by wuxian on 1/4/2017.
 */
public class Config {

    //7天全面抓一次
    public static final long GRAB_INTERNAL = -1;//1000 * 60 * 60 * 24 * 7;
    //上一次爬虫抓取的时间
    public static final String CONF_LASTGRAB = "/conf/lastgrab.txt";

    //存储区域的文件
    public static final String CONF_DISTINTC = "/conf/distinct.txt";

    //存储街道的文件
    public static final String CONF_AREA = "/conf/area.txt";

    public interface DB {

        String TABLE_DISTINCT_AREA = "area";  //杭州某个区下对应的街道

        String TABLE_AREA_COMPANY = "company";   //某个街道里的company
    }

}
