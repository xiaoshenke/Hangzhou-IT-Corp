package wuxian.me.lagouspider.biz.boss;

import okhttp3.Request;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.SpiderCallback;

/**
 * Created by wuxian on 6/5/2017.
 *
 * Todo: 准备抓boss的数据了,并且加入抓岗位数据功能
 * 注意搞个岗位的model
 */
public class BaseBossSpider extends BaseSpider {
    protected SpiderCallback getCallback() {
        return null;
    }

    protected Request buildRequest() {
        return null;
    }

    public int parseRealData(String s) {
        return 0;
    }

    protected boolean checkBlockAndFailThisSpider(String s) {
        return false;
    }

    public String name() {
        return null;
    }

    public String hashString() {
        return null;
    }
}
