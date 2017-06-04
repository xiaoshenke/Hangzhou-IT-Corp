package wuxian.me.lagouspider.biz.boss;

import com.sun.istack.internal.NotNull;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.BulletList;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.anti.MaybeBlockedException;
import wuxian.me.spidersdk.distribute.HttpUrlNode;
import wuxian.me.spidersdk.log.LogManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by wuxian on 13/5/2017.
 */
public class BPositionListSpider extends BaseBossSpider {

    public static BPositionListSpider fromUrlNode(HttpUrlNode node) {
        String url = BASE_URL + "c" + INDEX_HANGZHOU + "-p" + INDEX_POSITION + "/b_";
        if (!node.baseUrl.contains(url)) {
            return null;
        }

        if (!node.httpGetParam.containsKey("ka") || !node.httpGetParam.containsKey("page")) {
            return null;
        }

        int begin = node.baseUrl.indexOf("b_");
        if (begin == -1) {
            return null;
        }
        String distinc = node.baseUrl.substring(begin + 2, node.baseUrl.length() - 1);
        /*
        String reg = "(?<=/b_)*+(?=/)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(node.baseUrl);
        if (!matcher.find()) {
            return null;
        }
        String distinc = matcher.group();
        */
        int page = Integer.parseInt(node.httpGetParam.get("page"));
        return new BPositionListSpider(distinc, page);
    }

    public static HttpUrlNode toUrlNode(BPositionListSpider spider) {
        HttpUrlNode node = new HttpUrlNode();
        String url = buildNodeUrlFrom(spider);

        node.baseUrl = url;
        node.httpGetParam.put("page", String.valueOf(spider.page));
        node.httpGetParam.put("ka", "page-" + String.valueOf(spider.page));
        return node;
    }

    private static String buildNodeUrlFrom(BPositionListSpider spider) {
        return BASE_URL + "c" + INDEX_HANGZHOU + "-p" + INDEX_POSITION + "/b_" + spider.distinct + "/";
    }

    private static String BASE_URL = "http://www.zhipin.com/";

    public static String INDEX_HANGZHOU = CityTransformer.getCityCode(BossConfig.CITY_TO_SPIDER);
    public static String INDEX_POSITION = PositionTransformer.getPositionCode(BossConfig.POSITION_TO_SPIDER);
    private String distinct;
    private int page;

    public BPositionListSpider(@NotNull String distinct, int page) {
        this.distinct = distinct;
        this.page = page;
    }

    protected Request buildRequest() {
        String url = buildNodeUrlFrom(this);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("ka", "page-" + String.valueOf(page));
        urlBuilder.addQueryParameter("page", String.valueOf(page));

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

        HasAttributeFilter filter = new HasAttributeFilter("class", "job-list");
        NodeList list = parser.extractAllNodesThatMatch(filter);

        if (list != null && list.size() != 0) {

            Node node = list.elementAt(0);
            if (page == 1) {  //这是第一页 需要解析下position的数量
                String reg = "(?<=data-rescount=\")[0-9]+(?=\")";
                Pattern pattern = Pattern.compile(reg);
                String str = node.getText();
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()) {
                    long total = Long.parseLong(matcher.group());
                    LogManager.info("Current Now,number of total java job in " + distinct + " is " + total);
                    int num = (int) total / BossConfig.POSITION_NUM_PER_PAGE + 1;

                    for (int i = 2; i <= num; i++) {
                        Helper.dispatchSpider(new BPositionListSpider(distinct, i));
                    }
                }
            }

            list = node.getChildren();
            for (int i = 0; i < list.size(); i++) {
                node = list.elementAt(i);
                if (node instanceof BulletList && node.getText().trim().equals("ul")) {
                    list = node.getChildren();
                    for (int j = 0; j < list.size(); j++) {
                        Node child = list.elementAt(j);
                        if (child instanceof Bullet && child.getText().trim().equals("li")) {
                            //NodeLogUtil.printChildrenOfNode(child);
                            parsePosition(child);
                        }
                    }
                    break;
                }
            }
        }
    }

    private void parsePosition(Node node) {
        //LogManager.info("ParsePosition");
        String reg = "(?<=job_detail/)[0-9]+(?=.html)";
        Pattern pattern = Pattern.compile(reg);
        String str = node.toString();
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            long positionId = Long.parseLong(matcher.group());
            //LogManager.info("paresed position: "+positionId);

            if (BossConfig.ENABLE_SPIDE_DETAIL) {
                Helper.dispatchSpider(new BPositonDetailSpider(positionId));
            }
        }
    }

    public int parseRealData(String s) {
        try {
            parse(s);
        } catch (MaybeBlockedException e) {
            return BaseSpider.RET_MAYBE_BLOCK;
        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        }

        return BaseSpider.RET_SUCCESS;
    }

    public String name() {
        return "BossPositionListSpider: " + BossConfig.CITY_TO_SPIDER
                + " " + distinct + " page:" + page;
    }
}
