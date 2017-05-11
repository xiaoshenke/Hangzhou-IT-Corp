package wuxian.me.lagouspider.biz.zhishu;

import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.SpiderCallback;

/**
 * Created by wuxian on 6/5/2017.
 */
public abstract class BaseZhishuSpider extends BaseSpider {

    protected SpiderCallback getCallback() {
        return new ZhishuSpiderCallback(this);
    }

    //Todo
    protected boolean checkBlockAndFailThisSpider(String s) {
        return false;
    }

    public String hashString() {
        return name();
    }
}
