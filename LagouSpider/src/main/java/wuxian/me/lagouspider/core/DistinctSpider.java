package wuxian.me.lagouspider.core;

import static com.google.common.base.Preconditions.*;
import static wuxian.me.lagouspider.Config.URL_LAGOU_JAVA;
import static wuxian.me.lagouspider.util.Helper.*;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

import okhttp3.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.framework.FileUtil;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.framework.OkhttpProvider;

/**
 * Created by wuxian on 30/3/2017.
 * 抓取杭州所有的区信息,保存到@distinct.txt,area.txt
 * <p>
 */
public class DistinctSpider {
    public static final String CUT = ";";
    public static final String SEPRATE = ":";
    public DistinctSpider() {
    }

    public void beginSpider() {
        if (!distinctsFileValid()) {
            getDisticts();
            return;
        }
        if (!areaFileValid()) {
            getAreas();
        }
    }

    private void getAreas() {
        String distincts = FileUtil.readFromFile(getDistinctsFilePath());
        if (null == distincts) {
            return;
        }

        String[] dis = distincts.split(CUT);  //编码问题带来分解失败...

        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_JAVA)
                .newBuilder();
        urlBuilder.addQueryParameter("city", "杭州");
        String referer = urlBuilder.build().toString();

        for (int i = 0; i < dis.length; i++) {
            urlBuilder.removeAllQueryParameters("district");
            urlBuilder.addQueryParameter("district", dis[i]);
            Request request = new Request.Builder()
                    .headers(Helper.getHeaderBySpecifyRef(referer))
                    .url(urlBuilder.build().toString())
                    .build();

            final String distinct = dis[i];
            OkhttpProvider.getClient().newCall(request).enqueue(new Callback() {
                public void onFailure(Call call, IOException e) {
                }

                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        return;
                    }

                    final String data = response.body().string();
                    List<String> areas = parseArea(data);
                    writeArea(distinct, areas);
                }
            });
        }
    }

    private synchronized void writeArea(String distinct, List<String> areas) {
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

        }
    }

    private List<String> parseArea(String data) {
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

        } catch (ParserException e) {
            //Todo:
            //logger().error("parse areas from html error");
        }
        return distincts;
    }

    public static boolean areaFileValid() {
        return FileUtil.checkFileExist(getAreaFilePath());
    }

    private boolean distinctsFileValid() {
        return FileUtil.checkFileExist(getDistinctsFilePath());
    }

    private void getDisticts() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_JAVA)
                .newBuilder();
        urlBuilder.addQueryParameter("city", "杭州");

        final Request request = new Request.Builder()
                .headers(Helper.getHeaderBySpecifyRef("https://www.lagou.com/"))
                .url(urlBuilder.build().toString())
                .build();
        OkhttpProvider.getClient().newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }
                final String data = response.body().string();
                List<String> distincts = parseDistincts(data);
                if (distincts.size() != 0) {
                    String content = "";
                    for (String dis : distincts) {
                        content += dis;
                    }
                    FileUtil.writeToFile(getDistinctsFilePath(), content);
                }

                if (!areaFileValid()) {   //进行读取erea过程
                    getAreas();
                }
            }
        });
    }

    private List<String> parseDistincts(String data) {
        List<String> distincts = new ArrayList<String>();
        try {
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

        } catch (ParserException e) {
            //Todo
            //logger().error("parse distinct from html fail");
        }
        return distincts;
    }
}
