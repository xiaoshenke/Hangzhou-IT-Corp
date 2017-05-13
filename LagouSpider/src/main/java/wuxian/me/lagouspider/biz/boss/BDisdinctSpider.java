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
import wuxian.me.lagouspider.biz.lagou.DistinctSpider;
import wuxian.me.lagouspider.util.Config;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.NodeLogUtil;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.anti.MaybeBlockedException;
import wuxian.me.spidersdk.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import static wuxian.me.lagouspider.util.Helper.getBossDistinctsFilePath;
import static wuxian.me.lagouspider.util.Helper.getLagouAreaFilePath;
import static wuxian.me.lagouspider.util.Helper.getLagouDistinctsFilePath;

/**
 * Created by wuxian on 13/5/2017.
 * <p>
 * 拿到杭州区信息
 */
public class BDisdinctSpider extends BaseBossSpider {

    private static String BASE_URL = "http://www.zhipin.com/job_detail/";

    private static String INDEX_HANGZHOU = CityTransformer.getCityCode(BossConfig.CITY_TO_SPIDER);

    private List<String> distinctList = new ArrayList<String>();

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
            //NodeLogUtil.printChildrenOfNode(list.elementAt(0));

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
        //LogManager.info(s);
        try {
            parse(s);
        } catch (MaybeBlockedException e) {
            return BaseSpider.RET_MAYBE_BLOCK;
        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        }

        if (distinctList.size() != 0) {
            String content = "";
            for (String dis : distinctList) {
                content += dis + Config.CUT;
            }
            FileUtil.writeToFile(getBossDistinctsFilePath(), content);
        }

        return BaseSpider.RET_SUCCESS;
    }


    public String name() {
        return "BossDisdinctSpider: " + BossConfig.CITY_TO_SPIDER;
    }
}
