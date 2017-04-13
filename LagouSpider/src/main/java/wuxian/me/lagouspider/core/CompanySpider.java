package wuxian.me.lagouspider.core;

import okhttp3.Request;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.framework.BaseSpiderCallback;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.framework.OkhttpProvider;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * 准备抓取的内容
 * 1 招聘职位
 * 2 简历及时处理率
 * 3 面试评价
 * 4 面试评分
 * 5 融资情况 --> 如果是有融资情况 爬取IT橙子
 * 6 产品列表
 * 7 公司地址列表 --> 多个地址存入多地址表
 * 8 公司描述
 * 9 公司管理层信息？？ --> 认为似乎并没有什么卵用 因为履历一定是被夸大的 --> 后续可以考虑
 *
 */
public class CompanySpider extends BaseLagouSpider {
    long company_id = -1;

    private String selfDescription;
    int posionNum = -1;
    int resumeRate = -1;
    int interCommentNum = -1;  //面试评价个数
    int commentScore = -1;  //面试评价评分 --> 满分5分换算过来


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
