package wuxian.me.lagouspider.biz.lagou;

import static com.google.common.base.Preconditions.*;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.CUT;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.SEPRATE;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.SpiderUrl.*;
import static wuxian.me.lagouspider.util.Helper.*;

import okhttp3.*;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.util.FileUtil;

/**
 * Created by wuxian on 30/3/2017.
 * 抓取城市的所有的街道信息area.txt
 * <p>
 */
public class DistinctSpider extends BaseLagouSpider {
    String city;
    String distinct;

    public DistinctSpider(String city, String distinct) {
        this.city = city;
        this.distinct = distinct;
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_JAVA).newBuilder();
        urlBuilder.addQueryParameter("city", city);
        String referer = urlBuilder.build().toString();

        urlBuilder.removeAllQueryParameters("district");
        urlBuilder.addQueryParameter("district", distinct);
        Request request = new Request.Builder()
                .headers(Helper.getLagouHeader(referer, LagouConfig.SPIDER_NAME))
                .url(urlBuilder.build().toString())
                .build();

        return request;
    }

    public int parseRealData(String data) {

        List<String> distincts = new ArrayList<String>();
        try {
            Parser parser = new Parser(data);
            parser.setEncoding("utf-8");
            HasAttributeFilter filter = new HasAttributeFilter("class", "detail-bizArea-area");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Node tag = list.elementAt(i);
                    NodeList child = tag.getChildren();
                    for (int j = 0; j < child.size(); j++) {

                        Node node = child.elementAt(j);
                        if (node instanceof LinkTag) {
                            if (((LinkTag) node).getLinkText().equals("不限")) {
                                continue;
                            }
                            distincts.add(((LinkTag) node).getLinkText());
                        }
                    }
                }
            }
            writeArea(distinct, distincts);

        } catch (ParserException e) {

        }
        return BaseSpider.RET_SUCCESS;
    }

    public String name() {
        return "DistinctSpider: {" + city + " " + distinct + "}";
    }

    private void writeArea(String distinct, List<String> areas) {
        //LogManager.info("Begin to writeArea: " + areas.toString());
        synchronized (DistinctSpider.class) {
            checkNotNull(distinct);
            checkNotNull(areas);

            String former = "";
            if (FileUtil.checkFileExist(getAreaFilePath())) {
                former = FileUtil.readFromFile(getAreaFilePath());
            }

            String content = former;
            for (String area : areas) {
                content += distinct + SEPRATE + area + CUT;
            }

            content += "\n";
            if (!FileUtil.writeToFile(getAreaFilePath(), content)) {
                //LogManager.error("Error write content, content: "+content +" path: "+getAreaFilePath());

            }
        }

    }

}
