package wuxian.me.lagouspider.biz.boss;

/**
 * Created by wuxian on 7/5/2017.
 */
public interface BossConfig {

    int POSITION_NUM_PER_PAGE = 15;

    String CITY_TO_SPIDER = "杭州";

    String SPIDER_NAME = "BossSpider";

    String HOST = "www.zhipin.com";

    interface File {
        String CONF = "/conf/boss/";

        //存储区域的文件
        String DISTINTC = "distinct.txt";

        String POSITON_DES_PATH = "/boss_pos_des/";
    }

    interface TableName {

        String COMPANY = "bcompanies";

        String LOCATION = "blocations";

        String POSITION = "bpositions";
    }

    interface SaveDBThread {

        int SAVE_COMPANY_INTERVAL = 1000 * 1 * 1;

        int SAVE_POSITION_INTERVAL = 1000 * 1 * 1;

        int SAVE_LOCATION_INTERVAL = 1000 * 1 * 1;
    }

    interface EnableSaveDB {

        boolean ENABLE_SAVE_POSITION = true;

        boolean ENABLE_SAVE_LOCATION = true;

        boolean ENABLE_SAVE_COMPANY = true;

        boolean ENABLE_SAVE_COMPANY_DES = true;
    }

}
