package wuxian.me.lagouspider.biz.boss;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.apache.commons.lang3.time.DateUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.model.boss.BCompany;
import wuxian.me.lagouspider.model.boss.BLocation;
import wuxian.me.lagouspider.model.boss.BPosition;
import wuxian.me.lagouspider.save.boss.BCompanySaver;
import wuxian.me.lagouspider.save.boss.BLocationSaver;
import wuxian.me.lagouspider.save.boss.BPositionSaver;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.NodeLogUtil;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.anti.MaybeBlockedException;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.util.FileUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

                    NodeList list1 = child.getChildren();
                    for (int j = 0; j < list1.size(); j++) {
                        Node child1 = list1.elementAt(j);
                        if (child1 instanceof LinkTag) {
                            //NodeLogUtil.printChildrenOfNode(child1);

                            if (child1.getChildren() == null && child1.getChildren().size() == 0) {
                                break;
                            }

                            child1 = child1.getChildren().elementAt(0);
                            String str = child1.getText().trim();
                            int begin = str.indexOf("\"");
                            int end = str.indexOf("\"", begin + 1);
                            if (begin != -1 && end != -1) {
                                company.logo = str.substring(begin + 1, end);
                                LogManager.info("logo: " + company.logo);
                            }


                            break;
                        }
                    }
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
                                        company.sizeMin = Integer.parseInt(sizes[0]);
                                        company.sizeMax = Integer.parseInt(sizes[1]);
                                    } else {
                                        company.sizeMin = Integer.parseInt(size);//10000人以上
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

        if (company.companyId == -1) {
            throw new ParserException();
        }
    }

    private void parseLocation(String data) throws MaybeBlockedException, ParserException {
        Parser parser = new Parser(data);
        parser.setEncoding("utf-8");

        HasAttributeFilter filter = new HasAttributeFilter("class", "job-location");
        NodeList list = parser.extractAllNodesThatMatch(filter);

        if (list != null && list.size() != 0) {
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
                            location.lantitude = longlat[1];
                        }
                    }

                    continue;
                }

            }
        }

        if (location.location == null || location.location.length() == 0) {
            throw new ParserException("No Location found");
        }

    }

    private void parsePosition(String data)
            throws MaybeBlockedException, ParserException {

        Parser parser = new Parser(data);
        parser.setEncoding("utf-8");

        HasAttributeFilter filter = new HasAttributeFilter("class", "job-primary");
        NodeList list = parser.extractAllNodesThatMatch(filter);

        if (list == null && list.size() == 0) {
            return;
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
                        //NodeLogUtil.printNodeOnly(child1);

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
                                position.salaryMax = Integer.parseInt(sa);
                            } else if (sa.contains(Salary.YISHANG)) {
                                int index = sa.indexOf("K");
                                if (index != -1) {
                                    sa = sa.substring(0, index);
                                }
                                position.salaryMin = Integer.parseInt(sa);
                            } else {
                                sa = sa.replace("K", "");
                                String[] sas = sa.split("-");
                                if (sas.length == 2) {
                                    position.salaryMin = Integer.parseInt(sas[0]);
                                    position.salaryMax = Integer.parseInt(sas[1]);
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
                                position.city = child1.getText().trim();
                            } else if (k == 1) {
                                //应届生
                                String ex = child1.getText().trim();
                                if (ex.contains(Experience.YINGJIE)) {
                                    position.experienceMax = 0;  //0年经验
                                } else if (ex.contains(Experience.LESS_THAN_A_YEAR)) {
                                    int index = ex.indexOf("年");
                                    if (index != -1) {
                                        ex = ex.substring(0, index);
                                    }

                                    position.experienceMax = Integer.parseInt(ex);
                                } else if (ex.contains(Experience.MORE_THAN_TEN_YEAR)) {
                                    int index = ex.indexOf("年");
                                    if (index != -1) {
                                        ex = ex.substring(0, index);
                                    }

                                    position.experienceMin = Integer.parseInt(ex);
                                } else {
                                    int index = ex.indexOf("年");
                                    if (index != -1) {
                                        ex = ex.substring(0, index);
                                    }

                                    String[] exs = ex.split("-");

                                    if (exs.length == 2) {
                                        position.experienceMin = Integer.parseInt(exs[0]);
                                        position.experienceMax = Integer.parseInt(exs[1]);
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

    private void parseDes(String data)
            throws MaybeBlockedException, ParserException {
        Parser parser = new Parser(data);
        parser.setEncoding("utf-8");

        HasAttributeFilter filter = new HasAttributeFilter("class", "detail-content");
        NodeList list = parser.extractAllNodesThatMatch(filter);
        if (list == null || list.size() == 0) {
            return;
        }

        Node child = list.elementAt(0);
        list = child.getChildren();
        if (list == null || list.size() == 0) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            child = list.elementAt(i);
            if (child instanceof Div && child.getText().trim().contains("job-sec")) {
                NodeLogUtil.printChildrenOfNode(child);
                NodeList list1 = child.getChildren();
                if (list1 == null || list1.size() == 0) {
                    continue;
                }
                String type = "";
                for (int j = 0; j < list1.size(); j++) {
                    Node child1 = list1.elementAt(j);

                    if (child1 instanceof HeadingTag && child1.getText().trim().contains("h3")) {
                        if (child1.toPlainTextString().trim().contains("职位描述")) {
                            type = "zhiwei";
                            continue;
                        } else {   //这个tag是团队介绍的tag 这里不抓团队介绍
                            type = "tuandui";
                            continue;
                        }
                    }

                    if (child1 instanceof Div && child1.getText().trim().contains("text")) {
                        if (type.equals("zhiwei")) {
                            position.description = child1.toPlainTextString().trim();
                        } else if (type.equals("tuandui")) {
                            position.team = child1.toPlainTextString().trim();
                        }
                    }
                }
            }
        }
    }

    public static String formatPositionPostTime(String postTime) {

        String ret = null;
        String post = postTime;
        if (post == null || post.length() == 0) {
            return ret;
        }

        if (post.contains(":")) {
            ret = formattedToday;
            return ret;

        } else if (post.contains("月")) {
            String month = "";
            String day = "";
            String reg = "(?<=发布于)[0-9]+(?=月)";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(post);
            if (matcher.find()) {
                month = matcher.group();
            }

            String reg1 = "(?<=月)[0-9]+(?=日)";
            Pattern pattern1 = Pattern.compile(reg1);
            Matcher matcher1 = pattern1.matcher(post);
            if (matcher1.find()) {
                day = matcher1.group();
            }

            ret = String.valueOf(today.getYear() + 1900) + "-" + month + "-" + day;
        } else if (post.contains("昨天")) {
            ret = formattedYestoday;
        }

        return ret;

    }

    static Date today = DateUtils.truncate(Calendar.getInstance(), Calendar.DATE).getTime();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static String formattedToday = sdf.format(today);
    static String formattedYestoday = sdf.format(DateUtils.addDays(today, -1));


    public int parseRealData(String s) {
        try {
            parseCompany(s);

            parseLocation(s);

            parsePosition(s);

            parseDes(s);

            location.companyId = company.companyId;
            location.locationId = location.location.hashCode() + location.companyId;
            if (location.locationId < 0) {
                location.locationId = -(location.locationId + 1);   //Fixme:有没有必要
            }


            position.companyId = company.companyId;
            position.locationId = location.locationId;
            position.postTime = formatPositionPostTime(position.postTime);

            if (BossConfig.EnableSaveDB.ENABLE_SAVE_COMPANY) {
                saveCompany();
            }

            if (BossConfig.EnableSaveDB.ENABLE_SAVE_LOCATION) {
                saveLocation();
            }

            if (BossConfig.EnableSaveDB.ENABLE_SAVE_POSITION) {
                savePosition();
            }

            if (BossConfig.EnableSaveDB.ENABLE_SAVE_COMPANY_DES) {
                saveJobDescription();
            }

        } catch (MaybeBlockedException e) {
            return BaseSpider.RET_MAYBE_BLOCK;
        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        }

        return BaseSpider.RET_SUCCESS;
    }

    private void savePosition() {
        BPositionSaver.getInstance().saveModel(position);
    }

    private void saveCompany() {
        BCompanySaver.getInstance().saveModel(company);
    }

    private void saveLocation() {
        BLocationSaver.getInstance().saveModel(location);
    }

    private void saveJobDescription() {
        if (position.description == null || position.description.length() == 0) {
            return;
        }
        synchronized (BPositonDetailSpider.class) {
            if (!FileUtil.checkFileExist(getDesFilePath())) {
                FileUtil.writeToFile(getDesFilePath(), position.description);
            }
        }
    }

    private String getDesFilePath() {
        return FileUtil.getCurrentPath() + BossConfig.File.POSITON_DES_PATH + getDesFileName();
    }

    private String getDesFileName() {
        return position.positionName + "_" + position.positionId + ".txt";
    }

    public String name() {
        return "BossPositionDetailSpider: positionId: " + positionId;
    }
}
