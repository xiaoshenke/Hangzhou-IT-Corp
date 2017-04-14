package wuxian.me.lagouspider.core.itchengzi;

import okhttp3.Request;
import wuxian.me.lagouspider.framework.BaseSpider;

/**
 * Created by wuxian on 15/4/2017.
 * <p>
 * Todo
 */
public class SearchSpider extends BaseSpider {
    protected Request buildRequest() {
        return null;
    }

    public int parseRealData(String data) {
        return 0;
    }

    public boolean checkBlockAndFailThisSpider(int httpCode) {
        return false;
    }

    protected boolean checkBlockAndFailThisSpider(String html) {
        return false;
    }

    public String name() {
        return null;
    }

    public String fullName() {
        return null;
    }
}
