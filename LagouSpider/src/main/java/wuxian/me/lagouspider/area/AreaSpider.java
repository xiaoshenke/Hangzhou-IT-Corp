package wuxian.me.lagouspider.area;

import com.sun.istack.internal.NotNull;
import okhttp3.*;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.job.IJob;
import wuxian.me.lagouspider.job.JobProvider;
import wuxian.me.lagouspider.job.JobQueue;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.OkhttpProvider;

import java.io.IOException;
import java.util.List;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * 1 先拿到该地区的页数
 * 2 根据页数发送每一页数据的请求
 * 3 重试机制
 */
public class AreaSpider implements Runnable {
    private static final String URL_LAGOU_JAVA = "https://www.lagou.com/jobs/list_Java?px=default";
    Area area;
    private int pageNum = -1;

    public AreaSpider(@NotNull Area area) {
        this.area = area;
    }


    public void beginSpider() {
        for (int i = 0; i < pageNum; i++) {
            IJob job = JobProvider.getJob();
            job.setRealRunnable(new AreaPageSpider(area, i));
            JobQueue.getInstance().putJob(job);

            if (Helper.isTest) {
                break;
            }
        }
    }

    private void getCompanyPageNum() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_JAVA)
                .newBuilder();
        urlBuilder.addQueryParameter("city", "杭州");
        urlBuilder.addQueryParameter("district", area.distinct_name);
        String referer = urlBuilder.build().toString();

        urlBuilder.addQueryParameter("bizArea", area.name);
        Request request = new Request.Builder()
                .headers(Helper.getHeaderBySpecifyRef(referer))
                .url(urlBuilder.build().toString())
                .build();

        OkhttpProvider.getClient().newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure");
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }
                pageNum = parsePageNum(response.body().string());
                if (pageNum != -1) {
                    beginSpider();
                }
            }
        });
    }

    /**
     * @return -1表示解析失败
     */
    private int parsePageNum(String data) {

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
            } else {
                return 0;  //这个区域内没有公司 比如说灵隐区...
            }
        } catch (ParserException e) {

        }

        return -1;
    }

    public void run() {
        if (pageNum == -1) {
            getCompanyPageNum();
        } else {
            beginSpider();
        }
    }
}
