package wuxian.me.lagouspider.biz.zhishu;

/**
 * Created by wuxian on 7/5/2017.
 */
public interface ZhishuConfig {

    String SPIDER_NAME = "ZhishuSpider";

    String HOST = "index.baidu.com";

    interface SpiderUrl {

        String URL_ZHISHU = "https://index.baidu.com/?tpl=trend";
    }
}
