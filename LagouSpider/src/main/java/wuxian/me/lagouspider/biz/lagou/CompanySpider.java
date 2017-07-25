package wuxian.me.lagouspider.biz.lagou;

import com.sun.istack.internal.NotNull;
import okhttp3.Request;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import wuxian.me.lagouspider.model.lagou.Company;
import wuxian.me.lagouspider.model.lagou.Location;
import wuxian.me.lagouspider.model.lagou.Product;
import wuxian.me.lagouspider.save.lagou.CompanySaver;
import wuxian.me.lagouspider.save.lagou.LocationSaver;
import wuxian.me.lagouspider.save.lagou.ProductSaver;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.anti.MaybeBlockedException;

import java.util.ArrayList;
import java.util.List;

import static wuxian.me.lagouspider.biz.lagou.LagouConfig.EnableSaveDB.*;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.Location.LOCATION_MULTY;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.Location.LOCATION_NONE;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.Location.LOCATION_SINGLE;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.Spider.*;
import static wuxian.me.lagouspider.biz.lagou.LagouConfig.SpiderUrl.URL_LAGOU_COMPANY_MAIN;

/**
 * Created by wuxian on 30/3/2017.
 */
public class CompanySpider extends BaseLagouSpider {
    private NodeList trees = null;

    Company company;
    long company_id = -1;
    private String companyName;

    private String webLink;                 //company的主页url
    private String logo;
    private boolean lagouAuthentic = false;  //是否是拉勾认证的
    private String description;

    private String financeStage;             //A轮 B轮等信息
    String positionNum;                     //招聘岗位个数
    String score;                            //面试评价评分

    private List<Product> productList = new ArrayList<Product>();  //这个存入product database
    private List<String> locationList = new ArrayList<String>();  //如果是multi 存入locationDB

    private static final String REFERER = "https://www.lagou.com/zhaopin/Java/?labelWords=label";

    public CompanySpider(long company_id, String companyName) {
        super();
        this.company_id = company_id;
        this.companyName = companyName;
    }

    private String getUrl(long companyId) {
        return URL_LAGOU_COMPANY_MAIN + companyId + ".html";
    }

    protected Request buildRequest() {
        Request request = new Request.Builder()
                .headers(Helper.getLagouHeader(REFERER, LagouConfig.SPIDER_NAME))
                .url(getUrl(company_id))
                .build();
        return request;
    }

    public int parseRealData(String data) {
        try {
            Parser parser = new Parser(data);
            parser.setEncoding("utf-8");

            trees = parser.parse(null);
            parseBaseInfo();
            parseLocation();
            Company company = buildCompany();
            if (ENABLE_SAVE_COMPANY_DB) {
                saveCompany(company);
            }

            if (ENABLE_SAVE_PRODUCT_DB && false) {
                saveProduct();
            }

            if (ENABLE_SAVE_LOCATION_DB) {
                saveLocation();
            }

        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        } catch (MaybeBlockedException e) {
            return BaseSpider.RET_MAYBE_BLOCK;
        }

        return BaseSpider.RET_SUCCESS;
    }

    boolean parseBaseInfo() throws ParserException {

        HasAttributeFilter f1 = new HasAttributeFilter("class", "top_info_wrap");
        NodeList list = trees.extractAllNodesThatMatch(f1, true);
        if (list == null || list.size() == 0) {
            return true;
        }

        Node info_wrap = list.elementAt(0);
        list = info_wrap.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Node node = list.elementAt(i);
            if (node instanceof ImageTag) {
                logo = ((ImageTag) node).getImageURL();
                //logger().info("Logo: " + logo);
                break;
            }
        }

        HasAttributeFilter f2 = new HasAttributeFilter("class", "identification"); //拉勾认证
        NodeList ret = list.extractAllNodesThatMatch(f2, true);
        if (ret != null && ret.size() != 0) {
            lagouAuthentic = true;
        }

        HasAttributeFilter f12 = new HasAttributeFilter("class", "hovertips");
        ret = list.extractAllNodesThatMatch(f12, true);
        if (ret != null && ret.size() != 0) {
            webLink = ((LinkTag) ret.elementAt(0)).getLink().trim();
        }

        HasAttributeFilter f3 = new HasAttributeFilter("class", "company_word");
        ret = list.extractAllNodesThatMatch(f3, true);
        if (ret != null && ret.size() != 0) {
            description = ret.elementAt(0).toPlainTextString().trim();
        }


        HasAttributeFilter f4 = new HasAttributeFilter("class", "company_data");
        ret = list.extractAllNodesThatMatch(f4, true);
        if (ret != null && ret.size() != 0) {
            Node interview_data = ret.elementAt(0);
            HasAttributeFilter f5 = new HasAttributeFilter("class", "tipsys");
            ret = interview_data.getChildren().extractAllNodesThatMatch(f5, true);
            if (ret != null && ret.size() != 0) {
                for (int i = 0; i < ret.size(); i++) {
                    Node node = ret.elementAt(i);
                    if (i == 2) {
                        continue;
                    } else if (i > 3) {
                        break;
                    }
                    Node real = node.getPreviousSibling();

                    while (real != null) {
                        if (real instanceof TagNode) {
                            if (!real.getText().trim().contains("/strong")) {
                                real = real.getPreviousSibling();
                                continue;
                            }
                            Node pre = real.getPreviousSibling();
                            if (pre != null && pre instanceof TextNode) {
                                if (i == 0) {
                                    positionNum = pre.getText().trim();
                                    break;
                                }
                            }
                        }
                        real = real.getPreviousSibling();
                    }
                }
            }
        }

        HasAttributeFilter f5 = new HasAttributeFilter("id", "basic_container");
        list = trees.extractAllNodesThatMatch(f5, true);
        if (list != null && list.size() != 0) {
            Node child = list.elementAt(0);  //item_container

            if (child.getChildren() != null && child.getChildren().size() != 0) {

                HasAttributeFilter f7 = new HasAttributeFilter("class", "process");
                list = child.getChildren().extractAllNodesThatMatch(f7, true);

                if (list != null && list.size() != 0) {
                    Node type = list.elementAt(0);
                    type = type.getNextSibling();
                    while (type != null) {
                        if (type instanceof Span) {
                            financeStage = ((Span) type).getStringText().trim();
                            break;
                        }
                        type = type.getNextSibling();
                    }
                }
            }
        }

        HasAttributeFilter f9 = new HasAttributeFilter("class", "reviews-top");
        list = trees.extractAllNodesThatMatch(f9, true);
        if (list != null && list.size() != 0) {
            Node child = list.elementAt(0);  //reviews-top

            if (child.getChildren() != null && child.getChildren().size() != 0) {
                HasAttributeFilter f10 = new HasAttributeFilter("class", "score");
                list = child.getChildren().extractAllNodesThatMatch(f10, true);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0) {
                            score = ((Span) list.elementAt(i)).getStringText().trim();  //面试得分
                            break;
                        }
                    }
                }
            }
        }

        return true;
    }


    private void parseLocation() throws ParserException, MaybeBlockedException {
        HasAttributeFilter f1 = new HasAttributeFilter("class", "mlist_li_desc");
        NodeList list = trees.extractAllNodesThatMatch(f1, true);

        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                locationList.add(list.elementAt(i).toPlainTextString().trim());
            }
            return;
        } else {
            throw new MaybeBlockedException();
        }
    }

    private void saveLocation() {
        for (String location : locationList) {
            LocationSaver.getInstance().saveModel(new Location(company_id, location));
        }
    }

    private void saveProduct() {
        for (Product product : productList) {
            ProductSaver.getInstance().saveModel(product);
        }
    }

    private Company buildCompany() {
        if (company != null) {
            return company;
        }
        company = new Company(company_id);

        if (webLink.length() > LagouConfig.WEBLINK_MAX) {
            company.webLink = LagouConfig.SpiderUrl.URL_LAGOU_COMPANY_MAIN + company_id + ".html";
        } else {
            company.webLink = webLink;
        }

        company.logo = logo;
        company.lagouAuthentic = String.valueOf(lagouAuthentic);
        company.description = description;
        company.financeStage = financeStage;
        company.positionNum = positionNum;
        company.score = score;

        if (locationList.size() == 0) {
            company.detail_location = LOCATION_NONE;
        } else if (locationList.size() > 1) {
            company.detail_location = LOCATION_MULTY;
        } else {
            company.detail_location = LOCATION_SINGLE;  //先做这样的简化
        }
        return company;
    }

    private void saveCompany(@NotNull Company company) {
        CompanySaver.getInstance().saveModel(company);
    }

    public String name() {
        StringBuilder str = new StringBuilder("");
        for (String location : locationList) {
            str.append(location + ";");
        }
        return "CompanySpider: { " + "companyId: " + company_id + " " + companyName + " location: " + str + " ,招聘岗位: "
                + positionNum + " 面试评分: " + score + "}";
    }

    @Override
    public String hashString() {
        return "CompanySpider: " + company_id;
    }

}
