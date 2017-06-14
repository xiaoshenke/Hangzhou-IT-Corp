package wuxian.me.lagouspider.biz.zhishu;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidersdk.BaseSpider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by wuxian on 10/5/2017.
 * <p>
 * Fixme: 无法破解加载图片url的过程...
 */
public class ZhishuSpider extends BaseZhishuSpider {

    private String name;

    public ZhishuSpider(String name) {
        this.name = name;
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ZhishuConfig.SpiderUrl.URL_ZHISHU)
                .newBuilder();
        //urlBuilder.addQueryParameter("key", companyName);
        try {
            urlBuilder.addQueryParameter("word", URLEncoder.encode(name, "gb2312"));
        } catch (UnsupportedEncodingException e) {

        }

        final String referer = "https://www.baidu.com/";
        Headers headers = Helper.getZhishuHeader(referer, ZhishuConfig.SPIDER_NAME);

        Request request = new Request.Builder()
                .headers(headers)
                .url(urlBuilder.build().toString().replace("%25", "%"))
                .build();
        return request;
    }

    public int parseRealData(String s) {
        LogManager.info(s);
        return BaseSpider.RET_SUCCESS;
    }

    public String name() {
        return "ZhishuSpider: name: " + name;
    }
}
