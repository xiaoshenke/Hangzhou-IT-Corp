package wuxian.me.lagouspider.core;

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
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.model.Product;
import wuxian.me.lagouspider.util.Helper;

import java.util.List;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * 准备抓取的内容
 * 1 招聘职位
 * 2 简历及时处理率
 * 3 面试评价
 * 4 面试评分
 * 5 融资情况 --> 如果是有融资情况 爬取IT橙子
 * 6 产品列表
 * 7 公司地址列表 --> 多个地址存入多地址表
 * 8 公司描述
 * <p>
 * Todo:数据存表
 */
public class CompanySpider extends BaseLagouSpider {
    long company_id = -1;

    private String logo;
    private boolean lagouAuthen = false;  //是否是拉勾认证的
    private String selfDescription;
    private String companyBussiness;
    private String process;
    private String employeeNum;
    String posionNum;
    String resumeRate;
    String interCommentNum;  //面试评价个数

    String score;
    String accordSore;  //描述是否相符
    String interviewerScore;
    String environmentScore;

    //int commentScore = -1;  //面试评价评分 --> 满分5分换算过来
    private List<Product> productList;
    private List<String> locationList;

    private static final String REFERER = "https://www.lagou.com/zhaopin/Java/?labelWords=label";

    public CompanySpider(long company_id) {
        super();
        this.company_id = company_id;
    }

    private String getUrl(long companyId) {
        return Config.URL_LAGOU_COMPANY_MAIN + companyId + ".html";
    }

    boolean parseBaseInfo(Parser parser) throws ParserException {

        HasAttributeFilter f1 = new HasAttributeFilter("class", "top_info_wrap");
        NodeList list = trees.extractAllNodesThatMatch(f1, true);//= parser.extractAllNodesThatMatch(f1);
        if (list == null) {
            return true;
        }

        Node info_wrap = list.elementAt(0);
        NodeList info_wrap_list = info_wrap.getChildren();
        for (int i = 0; i < info_wrap_list.size(); i++) {
            Node node = info_wrap_list.elementAt(i);
            if (node instanceof ImageTag) {
                logo = ((ImageTag) node).getImageURL();
                logger().info("Logo: " + logo);
                break;
            }
        }

        HasAttributeFilter f2 = new HasAttributeFilter("class", "identification"); //拉勾认证
        NodeList ret = info_wrap_list.extractAllNodesThatMatch(f2, true);
        //Fixme:不知道为什么这边的api设计必须是NodeList而不是一个Node来调用extractAllxxx
        if (ret != null && ret.size() != 0) {
            lagouAuthen = true;
        }
        //logger().info("Authenticate by Laoug: " + lagouAuthen);

        HasAttributeFilter f3 = new HasAttributeFilter("class", "company_word");
        ret = info_wrap_list.extractAllNodesThatMatch(f3, true);
        if (ret != null && ret.size() != 0) {
            Node node = info_wrap_list.elementAt(0);
            if (node instanceof Div) {
                selfDescription = ((Div) node).getStringText();
                logger().info("selfDescripition: " + selfDescription);
            }
        }

        HasAttributeFilter f4 = new HasAttributeFilter("class", "company_data");
        ret = info_wrap_list.extractAllNodesThatMatch(f4, true);
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
                                    posionNum = pre.getText().trim();
                                    logger().info("招聘职位: " + posionNum);
                                } else if (i == 1) {
                                    resumeRate = pre.getText().trim();
                                    logger().info("简历处理率: " + resumeRate);
                                } else if (i == 3) {
                                    interCommentNum = pre.getText().trim();
                                    logger().info("面试评价: " + interCommentNum);
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
                HasAttributeFilter f6 = new HasAttributeFilter("class", "type");  //type
                list = child.getChildren().extractAllNodesThatMatch(f6, true);

                if (list != null && list.size() != 0) {
                    Node type = list.elementAt(0);
                    type = type.getNextSibling();
                    while (type != null) {
                        if (type instanceof Span) {
                            companyBussiness = ((Span) type).getStringText().trim();
                            break;
                        }
                        type = type.getNextSibling();
                    }
                }

                HasAttributeFilter f7 = new HasAttributeFilter("class", "process");  //type
                list = child.getChildren().extractAllNodesThatMatch(f7, true);

                if (list != null && list.size() != 0) {
                    Node type = list.elementAt(0);
                    type = type.getNextSibling();
                    while (type != null) {
                        if (type instanceof Span) {
                            process = ((Span) type).getStringText().trim();
                            break;
                        }
                        type = type.getNextSibling();
                    }
                }

                HasAttributeFilter f8 = new HasAttributeFilter("class", "number");  //type
                list = child.getChildren().extractAllNodesThatMatch(f8, true);

                if (list != null && list.size() != 0) {
                    Node type = list.elementAt(0);
                    type = type.getNextSibling();
                    while (type != null) {
                        if (type instanceof Span) {
                            employeeNum = ((Span) type).getStringText().trim();
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
                        } else if (i == 1) {
                            accordSore = ((Span) list.elementAt(i)).getStringText().trim();
                        } else if (i == 2) {
                            interviewerScore = ((Span) list.elementAt(i)).getStringText().trim();
                        } else if (i == 3) {
                            environmentScore = ((Span) list.elementAt(i)).getStringText().trim();
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
                //logger().info("Product logo: "+product.imgUrl);
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
                    product.url = ((LinkTag) child).getLinkText();
                    product.name = child.toPlainTextString().trim();
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
                        product.addType(child2.toPlainTextString().trim()); //Todo: 优化
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
        }

    }

    private void parseProductList(Parser parser) throws ParserException {
        logger().info("Begin to parse ProductList");
        HasAttributeFilter f1 = new HasAttributeFilter("id", "company_products");
        NodeList list = trees.extractAllNodesThatMatch(f1, true);//parser.extractAllNodesThatMatch(f1);
        if (list == null || list.size() == 0) {
            logger().info("No company_products found");
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
                    logger().info("Begin to parse Product: " + i);
                    parseProduct(ret.elementAt(i)); //解析每一个item 存入到类变量
                }
            }
        } else {
            logger().info("No item_content node found");
        }

    }

    private void parseLocation(Parser parser) throws ParserException {
        HasAttributeFilter f1 = new HasAttributeFilter("class", "mlist_li_desc");
        NodeList list = trees.extractAllNodesThatMatch(f1, true);

        logger().info("BEGIN to parse location");
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                printChildrenOfNode(list.elementAt(i));  //<p />
            }
        }
    }

    private NodeList trees = null;

    public int parseRealData(String data) {
        logger().info("CompanySpider, received htmlData,begin to pase");
        try {
            Parser parser = new Parser(data);
            parser.setEncoding("utf-8");

            trees = parser.parse(null);
            parseBaseInfo(parser);

            parser = new Parser(data);  //必须重新赋值一下
            parseProductList(parser);

            parser = new Parser(data);
            parseLocation(parser);

        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        }

        return BaseSpider.RET_SUCCESS;
    }

    protected Request buildRequest() {
        Request request = new Request.Builder()
                .headers(Helper.getHeaderBySpecifyRef(REFERER))
                .url(getUrl(company_id))
                .build();
        return request;
    }
}
