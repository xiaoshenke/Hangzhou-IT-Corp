package wuxian.me.lagouspider.business.lagou;

import com.sun.istack.internal.NotNull;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

import static wuxian.me.lagouspider.business.lagou.LagouConfig.CUT;
import static wuxian.me.lagouspider.business.lagou.LagouConfig.SpiderUrl.URL_LAGOU_JAVA;
import static wuxian.me.lagouspider.util.Helper.getAreaFilePath;
import static wuxian.me.lagouspider.util.Helper.getDistinctsFilePath;

/**
 * Created by wuxian on 2/5/2017.
 * 抓取城市的所有的区信息distinct.txt
 */
public class CitySpider extends BaseLagouSpider {

    private String city;

    public CitySpider() {
        this(LagouConfig.CITY_TO_SPIDER);
    }

    public CitySpider(@NotNull String city) {
        this.city = city;
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_JAVA)
                .newBuilder();
        urlBuilder.addQueryParameter("city", city);

        final Request request = new Request.Builder()
                .headers(Helper.getLagouHeader("https://www.lagou.com/", LagouConfig.SPIDER_NAME))
                .url(urlBuilder.build().toString())
                .build();
        return request;
    }

    private List<String> parseDistincts(String data) throws ParserException {
        List<String> distincts = new ArrayList<String>();
        Parser parser = new Parser(data);
        parser.setEncoding("utf-8");
        HasAttributeFilter filter = new HasAttributeFilter("class", "detail-district-area");

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
                        distincts.add(((LinkTag) node).getLinkText() + CUT);
                    }
                }
            }
        }
        return distincts;
    }

    public int parseRealData(String data) {
        try {
            List<String> distincts = parseDistincts(data);
            if (distincts.size() != 0) {
                String content = "";
                for (String dis : distincts) {
                    content += dis;
                }
                FileUtil.writeToFile(getDistinctsFilePath(), content);
            }

            if (!FileUtil.checkFileExist(getAreaFilePath()) && LagouConfig.Spider.ENABLE_SPIDER_DISTINCT) {
                for (String dis : distincts) {
                    DistinctSpider distinctSpider = new DistinctSpider(city, dis);
                    Helper.dispatchSpider(distinctSpider);
                }

            }

            return BaseSpider.RET_SUCCESS;
        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        }
    }

    public String name() {
        return "CitySpider: " + city;
    }
}
