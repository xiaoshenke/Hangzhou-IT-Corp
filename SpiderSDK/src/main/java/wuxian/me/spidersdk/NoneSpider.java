package wuxian.me.spidersdk;

import okhttp3.Request;
import wuxian.me.spidersdk.distribute.HttpUrlNode;

/**
 * Created by wuxian on 16/5/2017.
 */
public class NoneSpider extends BaseSpider {

    public static BaseSpider fromUrlNode(HttpUrlNode node) {
        return null;
    }

    //同上
    public static HttpUrlNode toUrlNode(NoneSpider spider) {
        return null;
    }

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
