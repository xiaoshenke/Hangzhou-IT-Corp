package wuxian.me.lagouspider.core;

import okhttp3.Request;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.OkhttpProvider;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 */
public class CompanySpider extends BaseLagouSpider {
    long company_id = -1;

    private static final String REFERER = "https://www.lagou.com/zhaopin/Java/?labelWords=label";

    public CompanySpider(long company_id) {
        this.company_id = company_id;
    }

    public void run() {
        beginSpider();
    }

    private void beginSpider() {
        Request request = new Request.Builder()
                .headers(Helper.getHeaderBySpecifyRef(REFERER))
                .url(getUrl(company_id))
                .build();

        OkhttpProvider.getClient().newCall(request).enqueue(new BaseSpiderCallback(this) {
            //Todo
            protected void parseResponseData(String data) {

            }
        });
    }

    private String getUrl(long companyId) {
        return Config.URL_LAGOU_COMPANY_MAIN + companyId + ".html";
    }
}
