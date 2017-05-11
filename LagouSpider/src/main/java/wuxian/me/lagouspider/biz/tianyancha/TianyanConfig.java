package wuxian.me.lagouspider.biz.tianyancha;

/**
 * Created by wuxian on 7/5/2017.
 */
public interface TianyanConfig {

    String SPIDER_NAME = "TianyanSpider";

    String HOST = "www.tianyancha.com";

    interface SpiderUrl {

        //String URL_LAGOU_COMPANY_MAIN = "https://www.lagou.com/gongsi/";
        //String URL_LAGOU_POSITION_JSON = "https://www.lagou.com/jobs/positionAjax.json?px=default";

        String URL_TIANYAN_SEARCH = "http://www.tianyancha.com/search?checkFrom=searchBox";
    }
}
