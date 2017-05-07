package wuxian.me.lagouspider.business.tianyancha;

import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.SpiderCallback;

/**
 * Created by wuxian on 7/5/2017.
 */
public abstract class BaseTianyanSpider extends BaseSpider {
    protected SpiderCallback getCallback() {
        return new TianyanSpiderCallback(this);
    }

    //Todo
    protected boolean checkBlockAndFailThisSpider(String s) {
        return false;
    }

    public String hashString() {
        return name();
    }
}
