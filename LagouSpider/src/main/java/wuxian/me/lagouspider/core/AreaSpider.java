package wuxian.me.lagouspider.core;

import com.sun.istack.internal.NotNull;
import okhttp3.*;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.framework.control.JobMonitor;
import wuxian.me.lagouspider.framework.SpiderCallback;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.framework.control.JobProvider;
import wuxian.me.lagouspider.framework.control.JobQueue;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.framework.OkhttpProvider;
import static wuxian.me.lagouspider.Config.URL_LAGOU_JAVA;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 */
public class AreaSpider extends BaseLagouSpider {
    Area area;
    private int pageNum = -1;

    public AreaSpider(@NotNull Area area) {
        this.area = area;
    }

    public void beginSpider() {
        for (int i = 1; i < pageNum; i++) {
            IJob job = JobProvider.getJob();
            job.setRealRunnable(new AreaPageSpider(area, i));
            JobQueue.getInstance().putJob(job);

            JobMonitor.getInstance().putJob(job, IJob.STATE_INIT);
        }
    }

    private void getCompanyPageNum() {
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

        OkhttpProvider.getClient().newCall(request).enqueue(new SpiderCallback(this));
    }

    /**
     * @return
     * -1:parsing error
     * 0: 没有内容 或者已经被block
     */
    private int parseData(String data) {
        try {
            Parser parser = new Parser(data);
            parser.setEncoding("utf-8");
            HasAttributeFilter filter = new HasAttributeFilter("class", "span totalNum");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Node tag = list.elementAt(i);
                    if (tag instanceof Span) {
                        return Integer.parseInt(((Span) tag).getStringText());
                    }
                }
            }

            return 0;  //这个区域内没有公司 比如说灵隐区...
        } catch (ParserException e) {
            return -1;
        }

    }

    public void run() {
        if (pageNum == -1) {
            getCompanyPageNum();
        } else {
            beginSpider();
        }
    }

    @Override
    public String toString() {
        return "AreaSpider: area is " + area.toString();
    }

    @Override
    public String name() {
        return "AreaSpider " + area.toString();
    }

    public boolean parseRealData(String data) {
        String body = data;
        pageNum = parseData(body);

        if (pageNum != -1) {
            if (pageNum == 0) {
                if (checkBlockAndFailThisSpider(body)) {
                    ;
                } else {
                    logger().info("We got zero page, " + name());
                }
            } else {
                logger().debug("Parsed num: " + pageNum + " " + simpleName());
                beginSpider();
            }
        } else {
            logger().error("parseData fail");
        }

        return true;
    }
}
