package wuxian.me.lagouspider.core.itchengzi;

import okhttp3.Request;
import wuxian.me.lagouspider.framework.BaseSpider;

/**
 * Created by wuxian on 15/4/2017.
 */
public class CompanyResultSpider extends BaseSpider {
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

    public String fullName() {
        return null;
    }
}
