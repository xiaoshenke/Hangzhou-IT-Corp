package wuxian.me.spidersdk;

import okhttp3.Request;
import wuxian.me.spidersdk.distribute.HttpUrlNode;
import wuxian.me.spidersdk.log.LogManager;

/**
 * Created by wuxian on 13/5/2017.
 */
public class NoneSpider extends BaseSpider {

    public static HttpUrlNode toUrlNode(NoneSpider spider) {
        LogManager.info("toUrlNode called");
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
