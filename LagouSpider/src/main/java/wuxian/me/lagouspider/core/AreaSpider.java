package wuxian.me.lagouspider.core;

import com.sun.istack.internal.NotNull;
import okhttp3.*;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.framework.control.MaybeBlockedException;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.framework.control.JobProvider;
import wuxian.me.lagouspider.framework.control.JobQueue;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.util.Helper;

import static wuxian.me.lagouspider.Config.URL_LAGOU_JAVA;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * 抓取某区域的公司页数 一个区域的公司可能不止一页,所以先拿到总的页数 然后分别对那一页的数据进行抓取@AreaPageSpider
 * <p>
 */
public class AreaSpider extends BaseLagouSpider {
    Area area;
    private int pageNum = -1;

    public AreaSpider(@NotNull Area area) {
        super();
        this.area = area;
    }

    public void beginSpiderAreaPage() {

        for (int i = 1; i < pageNum; i++) {
            IJob job = JobProvider.getJob();
            job.setRealRunnable(new AreaPageSpider(area, i));
            JobQueue.getInstance().putJob(job);
        }
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

    @Override
    public String toString() {
        return "AreaSpider: area is " + area.toString();
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_JAVA)
                .newBuilder();
        urlBuilder.addQueryParameter("city", "杭州");
        urlBuilder.addQueryParameter("district", area.distinct_name);
        final String referer = urlBuilder.build().toString();

        urlBuilder.addQueryParameter("bizArea", area.name);
        Request request = new Request.Builder()
                .headers(Helper.getHeaderBySpecifyRef(referer))
                .url(urlBuilder.build().toString())
                .build();
        return request;
    }

    @Override
    public String name() {
        return "AreaSpider " + area.toString();
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

        logger().debug("Parsed num: " + pageNum + " " + simpleName());

        if (Config.ENABLE_SPIDER_AREAPAGE) {
            beginSpiderAreaPage();
        }

        return BaseSpider.RET_SUCCESS;
    }
}
