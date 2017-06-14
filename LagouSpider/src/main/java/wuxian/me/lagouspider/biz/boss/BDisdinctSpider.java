package wuxian.me.lagouspider.biz.boss;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.DefinitionListBullet;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.util.Config;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.NodeLogUtil;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.model.HttpUrlNode;
import wuxian.me.spidercommon.util.FileUtil;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.anti.MaybeBlockedException;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import static wuxian.me.lagouspider.util.Helper.getBossDistinctsFilePath;

/**
 * Created by wuxian on 13/5/2017.
 * <p>
 * 拿到杭州区信息
 */
public class BDisdinctSpider extends BaseBossSpider {

    private static String BASE_URL = "http://www.zhipin.com/job_detail/";

    private static String INDEX_HANGZHOU = CityTransformer.getCityCode(BossConfig.CITY_TO_SPIDER);

    private List<String> distinctList = new ArrayList<String>();

    public static BDisdinctSpider fromUrlNode(HttpUrlNode node) {
        String url = BASE_URL;
        if (!node.baseUrl.contains(url)) {
            return null;
        }

        if (!node.httpGetParam.containsKey("query") || !node.httpGetParam.containsKey("scity")
                || !node.httpGetParam.containsKey("source")) {
            return null;
        }
        return new BDisdinctSpider();
    }

    public static HttpUrlNode toUrlNode(BDisdinctSpider spider) {
        HttpUrlNode node = new HttpUrlNode();
        String url = BASE_URL;

        node.baseUrl = url;
        node.httpGetParam.put("scity", INDEX_HANGZHOU);
        node.httpGetParam.put("query", "java");
        node.httpGetParam.put("source", "2");
        return node;
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("scity", INDEX_HANGZHOU);
        urlBuilder.addQueryParameter("query", "java");
        urlBuilder.addQueryParameter("source", "2");

        final String referer = "http://www.zhipin.com/";
        Headers headers = Helper.getBossHeader(referer, BossConfig.SPIDER_NAME);
        Request request = new Request.Builder()
                .headers(headers)
                .url(urlBuilder.build().toString())
                .build();
        return request;
    }

    private void parse(String data) throws MaybeBlockedException, ParserException {
        Parser parser = new Parser(data);
        parser.setEncoding("utf-8");

        HasAttributeFilter filter = new HasAttributeFilter("class", "condition-district show-condition-district");

        NodeList list = parser.extractAllNodesThatMatch(filter);
        if (list != null && list.size() != 0) {
            list = list.elementAt(0).getChildren();

            for (int i = 0; i < list.size(); i++) {
                Node node = list.elementAt(i);
                if (node instanceof DefinitionListBullet && node.getText().trim().equals("dd")) {
                    NodeList child = node.getChildren();
                    for (int j = 0; j < child.size(); j++) {
                        node = child.elementAt(j);
                        if (node instanceof LinkTag) {
                            if (((LinkTag) node).getLinkText().equals("不限")) {
                                continue;
                            }
                            distinctList.add(((LinkTag) node).getLinkText());
                        }
                    }
                    break;
                }
            }
        }
    }

    public int parseRealData(String s) {
        try {
            parse(s);
        } catch (MaybeBlockedException e) {

            LogManager.info("BDisdinctSpider RET_MAYBE_BLOCK");
            LogManager.info("-----------------------");
            LogManager.info(s);
            LogManager.info("-----------------------");
            return BaseSpider.RET_MAYBE_BLOCK;
        } catch (ParserException e) {

            LogManager.info("BDisdinctSpider RET_PARSING_ERR");
            LogManager.info("-----------------------");
            LogManager.info(s);
            LogManager.info("-----------------------");
            return BaseSpider.RET_PARSING_ERR;
        }

        if (distinctList.size() != 0) {
            String content = "";
            for (String dis : distinctList) {
                content += dis + Config.CUT;
            }
            FileUtil.writeToFile(getBossDistinctsFilePath(), content);
        }

        LogManager.info("BDisdinctSpider RET_SUCCESS");
        return BaseSpider.RET_SUCCESS;
    }


    public String name() {
        return "BossDisdinctSpider: " + BossConfig.CITY_TO_SPIDER;
    }
}
