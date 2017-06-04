package wuxian.me.lagouspider.biz.boss;

/**
 * Created by wuxian on 7/5/2017.
 */
public interface BossConfig {

    int POSITION_NUM_PER_PAGE = 15;

    String CITY_TO_SPIDER = "杭州";

    String POSITION_TO_SPIDER = "android";

    String SPIDER_NAME = "BossSpider";

    String HOST = "www.zhipin.com";

    boolean ENABLE_SPIDE_DETAIL = true;

    interface File {

        String FILE = "/file/boss/";

        //存储区域的文件
        String DISTINTC = "distinct.txt";

        String POSITON_DES_PATH = "/downloaded/boss/job_"+POSITION_TO_SPIDER+"/";
    }

    interface TableName {

        String COMPANY = "companies" + "_" + POSITION_TO_SPIDER;

        String LOCATION = "locations" + "_" + POSITION_TO_SPIDER;

        String POSITION = "positions" + "_" + POSITION_TO_SPIDER;
    }

    interface EnableSaveDB {

        boolean ENABLE_SAVE_POSITION = true;

        boolean ENABLE_SAVE_LOCATION = true;

        boolean ENABLE_SAVE_COMPANY = true;

        boolean ENABLE_SAVE_COMPANY_DES = true;

        //Todo
        boolean ENABLE_SAVE_TEAM_DES = true;
    }

}
