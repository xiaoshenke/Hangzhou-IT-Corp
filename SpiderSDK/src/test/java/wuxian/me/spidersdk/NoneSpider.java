package wuxian.me.spidersdk;

import okhttp3.Request;

/**
 * Created by wuxian on 13/5/2017.
 */
public class NoneSpider extends BaseSpider {
    protected SpiderCallback getCallback() {
        return null;
    }

    protected Request buildRequest() {
        return null;
    }

    public int parseRealData(String data) {
        return 0;
    }

    protected boolean checkBlockAndFailThisSpider(String html) {
        return false;
    }

    public String name() {
        return null;
    }

    public String hashString() {
        return null;
    }
}
