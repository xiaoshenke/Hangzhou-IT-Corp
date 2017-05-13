package wuxian.me.lagouspider.biz.boss;

import okhttp3.Request;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.SpiderCallback;

/**
 * Created by wuxian on 6/5/2017.
 * 注意搞个岗位的model
 */
public abstract class BaseBossSpider extends BaseSpider {

    protected SpiderCallback getCallback() {
        return new BossSpiderCallback(this);
    }

    protected boolean checkBlockAndFailThisSpider(String s) {
        return false;
    }

    public String hashString() {
        return name();
    }
}
