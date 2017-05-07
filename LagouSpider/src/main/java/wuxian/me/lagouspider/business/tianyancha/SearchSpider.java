package wuxian.me.lagouspider.business.tianyancha;

import com.sun.istack.internal.NotNull;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.log.LogManager;

/**
 * Created by wuxian on 7/5/2017.
 */
public class SearchSpider extends BaseTianyanSpider {

    private long lagouCompanyId;
    private String companyName;

    public SearchSpider(long lagouCompanyId, @NotNull String companyName) {

        this.lagouCompanyId = lagouCompanyId;
        this.companyName = companyName;
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(TianyanConfig.SpiderUrl.URL_TIANYAN_SEARCH)
                .newBuilder();
        urlBuilder.addQueryParameter("key", companyName);

        final String referer = "http://www.tianyancha.com/";
        Headers headers = Helper.getTianyanHeader(referer, TianyanConfig.SPIDER_NAME);

        Request request = new Request.Builder()
                .headers(headers)
                .url(urlBuilder.build().toString())
                .build();
        return request;
    }

    public int parseRealData(String s) {

        LogManager.info(s);
        return BaseSpider.RET_SUCCESS;
    }

    public String name() {
        return "SearchSpider: {lagouId: " + lagouCompanyId
                + " companyName: " + companyName + "}";
    }
}
