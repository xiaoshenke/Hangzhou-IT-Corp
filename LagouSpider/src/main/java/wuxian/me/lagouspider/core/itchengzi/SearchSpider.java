package wuxian.me.lagouspider.core.itchengzi;

import com.sun.istack.internal.NotNull;
import okhttp3.Request;
import wuxian.me.lagouspider.framework.BaseSpider;

/**
 * Created by wuxian on 15/4/2017.
 * <p>
 * Todo
 */
public class SearchSpider extends BaseSpider {

    private long companyId;
    private String companyName;

    public SearchSpider(@NotNull long companyId, @NotNull String companyName) {
        this.companyId = companyId;
        this.companyName = companyName;
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

    public String fullName() {
        return null;
    }
}
