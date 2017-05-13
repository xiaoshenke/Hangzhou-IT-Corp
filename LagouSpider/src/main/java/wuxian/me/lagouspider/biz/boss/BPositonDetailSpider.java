package wuxian.me.lagouspider.biz.boss;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.model.boss.BCompany;
import wuxian.me.lagouspider.model.boss.BLocation;
import wuxian.me.lagouspider.model.boss.BPosition;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.NodeLogUtil;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.anti.MaybeBlockedException;
import wuxian.me.spidersdk.log.LogManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuxian on 13/5/2017.
 * <p>
 */
public class BPositonDetailSpider extends BaseBossSpider {

    private BCompany company = new BCompany();
    private BLocation location = new BLocation();
    private BPosition position = new BPosition();

    private static String BASE_URL = "http://www.zhipin.com/job_detail/";
    private long positionId;

    public BPositonDetailSpider(long positionId) {
        this.positionId = positionId;

        position.positionId = positionId;
    }

    protected Request buildRequest() {

        String url = BASE_URL + positionId + ".html";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        final String referer = "http://www.zhipin.com/";
        Headers headers = Helper.getBossHeader(referer, BossConfig.SPIDER_NAME);
        Request request = new Request.Builder()
                .headers(headers)
                .url(urlBuilder.build().toString())
                .build();
        return request;

    }

    private void parseCompany(String data) throws MaybeBlockedException, ParserException {
        Parser parser = new Parser(data);
        parser.setEncoding("utf-8");

        HasAttributeFilter filter = new HasAttributeFilter("class", "info-company");
        NodeList list = parser.extractAllNodesThatMatch(filter);

        if (list != null && list.size() != 0) {
            Node node = list.elementAt(0);
            list = node.getChildren();
            boolean fisrt = true;
            for (int i = 0; i < list.size(); i++) {
                Node child = list.elementAt(i);
                if (child instanceof Div && child.getText().trim().contains("company-logo")) {
                    //logo节点 //暂时不要这个信息
                    continue;
                }

                if (child instanceof HeadingTag && child.getText().trim().contains("name")) {
                    if (child.getChildren() != null && child.getChildren().size() != 0) {
                        Node child2 = child.getChildren().elementAt(0);
                        company.name = child2.toPlainTextString().trim();

                        String reg = "(?<=gongsi/r?)[0-9]+(?=.html)";
                        Pattern pattern = Pattern.compile(reg);
                        Matcher matcher = pattern.matcher(child2.getText().trim());
                        if (matcher.find()) {
                            company.companyId = Long.parseLong(matcher.group());
                        }
                    }
                    continue;
                }

                if (child instanceof ParagraphTag) {
                    if (fisrt) {
                        fisrt = false;
                        company.fullName = child.toPlainTextString().trim();
                        continue;
                    } else {
                        NodeList list1 = child.getChildren();
                        int k = 0;
                        for (int j = 0; j < list1.size(); j++) {
                            child = list1.elementAt(j);
                            if (child instanceof TextNode) {
                                if (k == 0) {
                                    company.field = child.getText().trim();
                                } else if (k == 1) {
                                    company.financeStage = child.getText().trim();
                                } else if (k == 2) {
                                    String size = child.getText().trim();
                                    int index = size.indexOf("人");
                                    if (index != -1) {
                                        size = size.substring(0, index);
                                    }

                                    String[] sizes = size.split("-");
                                    if (size != null && sizes.length == 2) {
                                        company.sizeMin = sizes[0];
                                        company.sizeMax = sizes[1];
                                    } else {
                                        company.sizeMin = size;//10000人以上
                                    }
                                }
                                k++;
                            }
                        }
                        continue;
                    }
                }
            }
        }
    }

    private void parseLocation(String data) throws MaybeBlockedException, ParserException {
        Parser parser = new Parser(data);
        parser.setEncoding("utf-8");

        HasAttributeFilter filter = new HasAttributeFilter("class", "job-location");
        NodeList list = parser.extractAllNodesThatMatch(filter);

        if (list != null && list.size() != 0) {
            //NodeLogUtil.printChildrenOfNode(list.elementAt(0));
            list = list.elementAt(0).getChildren();

            for (int i = 0; i < list.size(); i++) {
                Node child = list.elementAt(i);

                if (child instanceof Div && child.getText().contains("location-address")) {
                    location.location = child.toPlainTextString().trim();

                    continue;
                }

                if (child instanceof Div && child.getText().contains("data-long-lat")) {
                    String reg = "(?<=data-long-lat=\")[0-9,.]+(?=\")";
                    Pattern pattern = Pattern.compile(reg);
                    Matcher matcher = pattern.matcher(child.getText().trim());
                    if (matcher.find()) {
                        String[] longlat = matcher.group().split(",");
                        if (longlat.length == 2) {
                            location.longitude = longlat[0];
                            location.latitude = longlat[1];
                        }
                    }

                    continue;
                }

            }
        }

    }

    private void parsePosition(String data)
            throws MaybeBlockedException, ParserException {

        Parser parser = new Parser(data);
        parser.setEncoding("utf-8");

        HasAttributeFilter filter = new HasAttributeFilter("class", "job-primary");
        NodeList list = parser.extractAllNodesThatMatch(filter);

        if (list == null && list.size() == 0) {
            return;  //Fixme
        }

        Node jobPrimary = list.elementAt(0);
        HasAttributeFilter filter2 = new HasAttributeFilter("class", "info-primary");

        list = jobPrimary.getChildren().extractAllNodesThatMatch(filter2, true);

        if (list != null && list.size() != 0) {
            list = list.elementAt(0).getChildren();
            for (int i = 0; i < list.size(); i++) {
                Node child = list.elementAt(i);

                if (child instanceof Div && child.getText().trim().contains("job-author")) {
                    position.postTime = child.toPlainTextString().trim();

                    continue;
                }

                if (child instanceof Div && child.getText().trim().contains("name")) {
                    NodeList list1 = child.getChildren();
                    int k = 0;

                    for (int j = 0; j < list1.size(); j++) {
                        Node child1 = list1.elementAt(j);
                        NodeLogUtil.printNodeOnly(child1);

                        if (child1 instanceof TextNode) {
                            if (k == 0) {
                                position.positionName = child1.getText().trim();
                            }
                            k++;
                        }

                        if (child1 instanceof Span) {
                            //18k-30k
                            String sa = child1.toPlainTextString().trim();

                            if (sa.contains(Salary.YIXIA)) {
                                int index = sa.indexOf("K");
                                if (index != -1) {
                                    sa = sa.substring(0, index);
                                }
                                position.salaryMax = sa;
                            } else if (sa.contains(Salary.YISHANG)) {
                                int index = sa.indexOf("K");
                                if (index != -1) {
                                    sa = sa.substring(0, index);
                                }
                                position.salaryMin = sa;
                            } else {
                                sa = sa.replace("K", "");
                                String[] sas = sa.split("-");
                                if (sas.length == 2) {
                                    position.salaryMin = sas[0];
                                    position.salaryMax = sas[1];
                                }
                            }
                        }
                    }


                    continue;
                }

                if (child instanceof ParagraphTag && child.getText().trim().contains("p")) {

                    NodeList list1 = child.getChildren();
                    int k = 0;
                    for (int j = 0; j < list1.size(); j++) {
                        Node child1 = list1.elementAt(j);

                        if (child1 instanceof TextNode) {
                            if (k == 0) {

                            } else if (k == 1) {
                                //应届生
                                String ex = child1.getText().trim();
                                if (ex.contains(Experience.YINGJIE)) {
                                    position.experienceMax = "0";  //0年经验
                                } else if (ex.contains(Experience.LESS_THAN_A_YEAR)) {
                                    int index = ex.indexOf("年");
                                    if (index != -1) {
                                        ex = ex.substring(0, index);
                                    }

                                    position.experienceMax = ex;
                                } else if (ex.contains(Experience.MORE_THAN_TEN_YEAR)) {
                                    int index = ex.indexOf("年");
                                    if (index != -1) {
                                        ex = ex.substring(0, index);
                                    }

                                    position.experienceMin = ex;
                                } else {
                                    int index = ex.indexOf("年");
                                    if (index != -1) {
                                        ex = ex.substring(0, index);
                                    }

                                    String[] exs = ex.split("-");

                                    if (exs.length == 2) {
                                        position.experienceMin = exs[0];
                                        position.experienceMax = exs[1];
                                    }

                                }

                            } else if (k == 2) {
                                position.education = child1.getText().trim();
                            }
                            k++;
                        }
                    }
                    continue;
                }
            }
        }
    }


    //Todo: company location position存入数据库
    public int parseRealData(String s) {
        try {
            parseCompany(s);

            parseLocation(s);

            parsePosition(s);

        } catch (MaybeBlockedException e) {
            return BaseSpider.RET_MAYBE_BLOCK;
        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        }

        return BaseSpider.RET_SUCCESS;
    }

    public String name() {
        return "BossPositionDetailSpider: positionId: " + positionId;
    }
}
