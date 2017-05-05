package wuxian.me.lagouspider.core;

import com.sun.istack.internal.NotNull;
import okhttp3.*;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.JobManager;
import wuxian.me.spidersdk.anti.MaybeBlockedException;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.log.LogManager;

import static wuxian.me.lagouspider.Config.Spider.ENABLE_SPIDER_AREAPAGE;
import static wuxian.me.lagouspider.Config.SpiderUrl.URL_LAGOU_JAVA;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * 抓取某区域的公司页数 一个区域的公司可能不止一页,所以先拿到总的页数 然后分别对那一页的数据进行抓取@AreaPageSpider
 * <p>
 */
public class AreaSpider extends BaseLagouSpider {
    Area area;
    private int pageNum = -1;
    private String agent = null;

    public AreaSpider(@NotNull Area area) {
        super();
        this.area = area;
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_JAVA)
                .newBuilder();
        urlBuilder.addQueryParameter("city", "杭州");
        urlBuilder.addQueryParameter("district", area.distinct_name);
        final String referer = urlBuilder.build().toString();

        urlBuilder.addQueryParameter("bizArea", area.name);
        Headers headers = Helper.getHeaderBySpecifyRef(referer);
        if (agent != null) {
            headers = headers.newBuilder().set("User-Agent", agent).build();
        }
        Request request = new Request.Builder()
                .headers(headers)
                .url(urlBuilder.build().toString())
                .build();
        return request;
    }

    public int parseRealData(String data) {
        String body = data;
        try {
            pageNum = parseData(body);
        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        } catch (MaybeBlockedException e) {
            return BaseSpider.RET_MAYBE_BLOCK;
        }
        LogManager.debug("Parsed num: " + pageNum + " " + simpleName());
        if (ENABLE_SPIDER_AREAPAGE) {
            beginSpiderAreaPage();
        }
        return BaseSpider.RET_SUCCESS;
    }

    private int parseData(String data) throws ParserException, MaybeBlockedException {
        Parser parser = new Parser(data);
        parser.setEncoding("utf-8");
        HasAttributeFilter filter = new HasAttributeFilter("class", "span totalNum");
        NodeList list = parser.extractAllNodesThatMatch(filter);
        if (list != null && list.size() != 0) {
            Node tag = list.elementAt(0);
            return Integer.parseInt(((Span) tag).getStringText().trim());

        } else {
            throw new MaybeBlockedException();
        }
    }

    public void beginSpiderAreaPage() {
        for (int i = 1; i < pageNum; i++) {
            IJob job = JobProvider.getJob();
            job.setRealRunnable((new AreaPageSpider(area, i)));
            JobManager.getInstance().putJob(job);
        }
    }

    @Override
    public String name() {
        return "AreaSpider: {" + area.name() + "}";
    }

}
