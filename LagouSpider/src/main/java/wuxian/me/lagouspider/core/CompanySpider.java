package wuxian.me.lagouspider.core;

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
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.core.itjuzi.SearchSpider;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.framework.control.JobProvider;
import wuxian.me.lagouspider.framework.control.JobQueue;
import wuxian.me.lagouspider.framework.control.MaybeBlockedException;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.model.Product;
import wuxian.me.lagouspider.save.CompanySaver;
import wuxian.me.lagouspider.save.ICompanySaver;
import wuxian.me.lagouspider.util.Helper;

import java.util.ArrayList;
import java.util.List;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;
import static wuxian.me.lagouspider.util.NodeLogUtil.*;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * Todo: 存储product,location
 */
public class CompanySpider extends BaseLagouSpider {
    long company_id = -1;

    private String companyBussiness;  //和AreaPageSpider重复
    private String companyName;
    private String companySize;         //和AreaPageSpider重复

    private String webLink; //company的主页url
    private String logo;
    private boolean lagouAuthentic = false;  //是否是拉勾认证的
    private String description;

    private String financeStage;  //A轮 B轮等信息
    String positionNum;  //招聘岗位个数
    String resumeRate;   //简历处理及时率
    String interviewNum;  //面试评价个数

    String score;       //面试评价评分
    String accordSore;  //描述是否相符
    String interviewerScore; //面试官评分
    String environmentScore; //环境评分

    private List<Product> productList = new ArrayList<Product>();  //这个存入product database
    private List<String> locationList = new ArrayList<String>();  //如果是multi 存入locationDB

    private static final String REFERER = "https://www.lagou.com/zhaopin/Java/?labelWords=label";

    public CompanySpider(long company_id, String companyName) {
        super();
        this.company_id = company_id;
        this.companyName = companyName;
    }

    private String getUrl(long companyId) {
        return Config.URL_LAGOU_COMPANY_MAIN + companyId + ".html";
    }

    boolean parseBaseInfo() throws ParserException {

        HasAttributeFilter f1 = new HasAttributeFilter("class", "top_info_wrap");
        NodeList list = trees.extractAllNodesThatMatch(f1, true);
        if (list == null) {
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
        //logger().info("Authenticate by Lagou: " + lagouAuthentic);

        HasAttributeFilter f12 = new HasAttributeFilter("class", "hovertips");
        ret = list.extractAllNodesThatMatch(f12, true);
        //logger().info("BEGIN to parse company name");
        if (ret != null && ret.size() != 0) {
            //companyName = ((LinkTag) ret.elementAt(0)).getAttribute("title").trim();
            //logger().info("companyName: " + companyName);

            webLink = ((LinkTag) ret.elementAt(0)).getLink().trim();

            if (Config.ENABLE_SPIDER_ITCHENGZI_SEARCH) {
                IJob iJob = JobProvider.getJob();
                iJob.setRealRunnable(new SearchSpider(company_id, companyName));
                JobQueue.getInstance().putJob(iJob);
            }
        }


        HasAttributeFilter f3 = new HasAttributeFilter("class", "company_word");
        ret = list.extractAllNodesThatMatch(f3, true);
        if (ret != null && ret.size() != 0) {
            printChildrenOfNode(ret.elementAt(0));
            description = ret.elementAt(0).toPlainTextString().trim();
            //logger().info("selfDescripition: " + description);
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
                                    //logger().info("招聘职位: " + positionNum);
                                } else if (i == 1) {
                                    resumeRate = pre.getText().trim();
                                    //logger().info("简历处理率: " + resumeRate);
                                } else if (i == 3) {
                                    interviewNum = pre.getText().trim();
                                    //logger().info("面试评价: " + interviewNum);
                                }
                                break;
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

                /*  //和@AreaPageSpider重复
                HasAttributeFilter f6 = new HasAttributeFilter("class", "type");  //type
                list = child.getChildren().extractAllNodesThatMatch(f6, true);
                if (list != null && list.size() != 0) {
                    Node type = list.elementAt(0);
                    type = type.getNextSibling();
                    while (type != null) {
                        if (type instanceof Span) {
                            companyBussiness = ((Span) type).getStringText().trim();
                            logger().info("companyBussiness: " + companyBussiness);
                            break;
                        }
                        type = type.getNextSibling();
                    }
                }
                */

                HasAttributeFilter f7 = new HasAttributeFilter("class", "process");
                list = child.getChildren().extractAllNodesThatMatch(f7, true);

                if (list != null && list.size() != 0) {
                    Node type = list.elementAt(0);
                    type = type.getNextSibling();
                    while (type != null) {
                        if (type instanceof Span) {
                            financeStage = ((Span) type).getStringText().trim();
                            //logger().info("financeStage: " + financeStage);
                            break;
                        }
                        type = type.getNextSibling();
                    }
                }

                /*  //和@AreaPageSpider重复
                HasAttributeFilter f8 = new HasAttributeFilter("class", "number");
                list = child.getChildren().extractAllNodesThatMatch(f8, true);
                if (list != null && list.size() != 0) {
                    Node type = list.elementAt(0);
                    type = type.getNextSibling();
                    while (type != null) {
                        if (type instanceof Span) {
                            companySize = ((Span) type).getStringText().trim();
                            logger().info("companySize: " + companySize);
                            break;
                        }
                        type = type.getNextSibling();
                    }
                }
                */
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
                            //logger().info("面试得分: " + score);
                        } else if (i == 1) {
                            accordSore = ((Span) list.elementAt(i)).getStringText().trim();
                            //logger().info("面试符合: " + accordSore);
                        } else if (i == 2) {
                            interviewerScore = ((Span) list.elementAt(i)).getStringText().trim();
                            //logger().info("面试官评分: " + interviewerScore);
                        } else if (i == 3) {
                            environmentScore = ((Span) list.elementAt(i)).getStringText().trim();
                            //logger().info("环境得分: " + environmentScore);
                        }
                    }
                }
            }
        }

        return true;
    }

    private void parseProduct(final Node node) throws ParserException {
        Product product = new Product(company_id);
        NodeList list = node.getChildren();
        if (list == null) {
            //logger().info("Node has no children, return");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Node child = list.elementAt(i);
            //printNodeOnly(child);
            if ((child instanceof ImageTag)) {
                product.imgUrl = ((ImageTag) child).getImageURL(); //产品logo --> success
                //logger().info("Product logo: " + product.imgUrl);
                break;
            }
        }

        Node child = null;
        //logger().info("BEGIN to parse product_url");
        HasAttributeFilter f1 = new HasAttributeFilter("class", "product_url");
        NodeList ret = list.extractAllNodesThatMatch(f1, true);
        if (ret != null && ret.size() != 0) {
            child = ret.elementAt(0);  //这是product_url node
            //printChildrenOfNode(child);
            ret = child.getChildren();
            for (int i = 0; i < ret.size(); i++) {
                child = ret.elementAt(i);
                if (child instanceof LinkTag) {
                    product.url = ((LinkTag) child).getLink();
                    product.name = child.toPlainTextString().trim();

                    //logger().info("product url: " + product.url);
                    //logger().info("product name: " + product.name);
                    break;
                }
            }
        }

        //logger().info("BEGIN to parse clearfix");
        HasAttributeFilter f2 = new HasAttributeFilter("class", "clearfix");
        ret = list.extractAllNodesThatMatch(f2, true);  //product type
        if (ret != null && ret.size() != 0) {
            child = ret.elementAt(0);
            ret = child.getChildren();
            if (ret != null) {
                for (int i = 0; i < ret.size(); i++) {
                    Node child2 = ret.elementAt(i);
                    if (child2 instanceof Bullet) {
                        String productType = child2.toPlainTextString().trim();
                        product.addLabel(productType);
                        //logger().info("product type: " + productType);
                    }
                }
            }
        }

        //logger().info("BEGIN to parse product_profile");//解析product description
        HasAttributeFilter f3 = new HasAttributeFilter("class", "product_profile");
        ret = list.extractAllNodesThatMatch(f3, true);
        if (ret != null && ret.size() != 0) {
            child = ret.elementAt(0);
            product.description = child.toPlainTextString().trim();

            //logger().info("product description: " + product.description);
        }

        productList.add(product);
    }

    private void parseProductList() throws ParserException {
        //logger().info("Begin to parse ProductList");
        HasAttributeFilter f1 = new HasAttributeFilter("id", "company_products");
        NodeList list = trees.extractAllNodesThatMatch(f1, true);//parser.extractAllNodesThatMatch(f1);
        if (list == null || list.size() == 0) {
            //logger().info("No company_products found");
            return;
            //throw new ParserException("No Company_products found");
        }

        Node product = list.elementAt(0);
        //printNodeOnly(product);
        HasAttributeFilter f2 = new HasAttributeFilter("class", "item_content");
        NodeList ret = product.getChildren().extractAllNodesThatMatch(f2, true);
        if (ret != null && ret.size() != 0) {
            product = ret.elementAt(0);  //item_content的子节点就是product节点
            ret = product.getChildren();
            if (ret != null && ret.size() != 0) {
                for (int i = 0; i < ret.size(); i++) {
                    //logger().info("Begin to parse Product: " + i);
                    parseProduct(ret.elementAt(i)); //解析每一个item 存入到类变量
                }
            }
        } else {
            //logger().info("No item_content node found");
        }

    }

    private void parseLocation() throws ParserException, MaybeBlockedException {
        HasAttributeFilter f1 = new HasAttributeFilter("class", "mlist_li_desc");
        NodeList list = trees.extractAllNodesThatMatch(f1, true);

        //logger().info("BEGIN to parse location");
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                //printChildrenOfNode(list.elementAt(i));  //<p />
                locationList.add(list.elementAt(i).toPlainTextString().trim());
            }
            return;
        } else {
            throw new MaybeBlockedException();
        }
    }

    private NodeList trees = null;

    public int parseRealData(String data) {
        logger().info("CompanySpider, received htmlData,begin to pase");
        try {
            Parser parser = new Parser(data);
            parser.setEncoding("utf-8");

            trees = parser.parse(null);
            parseBaseInfo();

            //parser = new Parser(data);  //必须重新赋值一下
            parseProductList();

            parseLocation();

            Company company = buildCompany();
            if (Config.ENABLE_SAVE_COMPANY_DB) {
                saveCompany(company);
            } else {
                logger().info("Company main: " + company.toString());
            }

            if (Config.ENABLE_SAVE_PRODUCT_DB) {
                saveProduct();
            } else {
                StringBuilder str = new StringBuilder("");
                for (Product product : productList) {
                    str.append(product.toString() + ", ");
                }
                logger().info("ProductList: " + str.toString());
            }

            if (Config.ENABLE_SAVE_LOCATION_DB) {
                saveLocation();
            } else {
                StringBuilder str = new StringBuilder("");
                for (String location : locationList) {
                    str.append(location.toString() + ", ");
                }
                logger().info("LocationList: ");
            }

            if (Config.ENABLE_SPIDER_ITCHENGZI_SEARCH) {
                beginITJuziSearchSpider();
            }

        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        } catch (MaybeBlockedException e) {
            return BaseSpider.RET_MAYBE_BLOCK;
        }

        return BaseSpider.RET_SUCCESS;
    }

    //Todo:判断是A轮以上 那么进行IT桔子搜索
    private void beginITJuziSearchSpider() {
        ;
    }

    //Todo
    private void saveLocation() {
        ;
    }

    //Todo
    private void saveProduct() {
        ;
    }

    private Company buildCompany() {
        Company company = new Company(company_id);

        company.webLink = webLink;
        company.logo = logo;
        company.lagouAuthentic = lagouAuthentic;
        company.description = description;
        company.financeStage = financeStage;
        company.positionNum = positionNum;
        company.resumeRate = resumeRate;
        company.interviewNum = interviewNum;
        company.score = score;
        company.accordSore = accordSore;
        company.interviewerScore = interviewerScore;
        company.environmentScore = environmentScore;

        if (locationList.size() == 0) {
            company.detail_location = ICompanySaver.LOCATION_NONE;
        } else if (locationList.size() > 1) {
            company.detail_location = ICompanySaver.LOCATION_MULTY;
        } else {
            company.detail_location = locationList.get(0);
        }
        return company;
    }

    private void saveCompany(@NotNull Company company) {
        CompanySaver.getInstance().saveCompany(company);
    }

    protected Request buildRequest() {
        Request request = new Request.Builder()
                .headers(Helper.getHeaderBySpecifyRef(REFERER))
                .url(getUrl(company_id))
                .build();
        return request;
    }
}
