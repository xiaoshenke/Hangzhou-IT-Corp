package wuxian.me.proxyspider.xun;

import okhttp3.Request;
import wuxian.me.spidercommon.model.HttpUrlNode;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.SpiderCallback;

/**
 * Created by wuxian on 20/6/2017.
 */
public class XunProxySpider extends BaseSpider {

    public static HttpUrlNode toUrlNode(XunProxySpider spider) {
        return null;
    }

    public static XunProxySpider fromUrlNode(HttpUrlNode node) {
        return null;
    }

    protected SpiderCallback getCallback() {
        return new XunSpiderCallback(this);
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
        return "XunProxySpider";
    }

    public String hashString() {
        return null;
    }
}
