package wuxian.me.lagouspider.biz.boss;

/**
 * Created by wuxian on 7/5/2017.
 */
public interface BossConfig {


    String CITY_TO_SPIDER = "杭州";

    String SPIDER_NAME = "BossSpider";

    String HOST = "www.zhipin.com";

    interface File {

        String CONF = "/conf/boss/";

        //存储区域的文件
        String DISTINTC = "distinct.txt";

    }

}
