package wuxian.me.lagouspider;

import static com.google.common.base.Preconditions.*;

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
import wuxian.me.lagouspider.util.FileUtil;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.OkhttpProvider;

/**
 * Created by wuxian on 30/3/2017.
 * 抓取杭州所有的区信息,保存到@distinct.txt,area.txt
 * <p>
 */
public class HangzhouAreasSpider {
    private static final String URL_LAGOU_HANGZHOU_JAVA = "https://www.lagou.com/jobs/list_Java?px=default";
    public static final String CUT = ";";
    public static final String SEPRATE = ":";

    private final OkHttpClient client = OkhttpProvider.getClient();

    private Headers distinctsHeaders;

    public HangzhouAreasSpider() {
        distinctsHeaders = Helper.getHeaderBySpecifyRef("https://www.lagou.com/");
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
        String distincts = FileUtil.readFromFile(FileUtil.getDistinctsFilePath());
        if (null == distincts) {
            System.out.println("read distincts file error");
        }

        String[] dis = distincts.split(CUT);  //编码问题带来分解失败...

        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_HANGZHOU_JAVA)
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
            client.newCall(request).enqueue(new Callback() {
                public void onFailure(Call call, IOException e) {
                    System.out.println("onFailure");
                }

                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        System.out.println("onResponse fail");
                    }
                    System.out.println("onResponse success");

                    final String data = response.body().string();
                    //System.out.println(data);
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
        if (FileUtil.checkFileExist(FileUtil.getAreaFilePath())) {
            former = FileUtil.readFromFile(FileUtil.getAreaFilePath());
        }

        String content = former;
        for (String area : areas) {
            content += distinct + SEPRATE + area + CUT;
        }

        content += "\n";

        if (!FileUtil.writeToFile(FileUtil.getAreaFilePath(), content)) {
            System.out.println("writearea error");
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
            System.out.println("parseDistincts success");
        } catch (ParserException e) {
            System.out.println("parseDistincts error");
        }
        return distincts;
    }

    public static boolean areaFileValid() {
        return FileUtil.checkFileExist(FileUtil.getAreaFilePath());
    }

    private boolean distinctsFileValid() {
        return FileUtil.checkFileExist(FileUtil.getDistinctsFilePath());
    }

    private void getDisticts() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_HANGZHOU_JAVA)
                .newBuilder();
        urlBuilder.addQueryParameter("city", "杭州");

        final Request request = new Request.Builder()
                .headers(distinctsHeaders)
                .url(urlBuilder.build().toString())
                .build();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure");
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("onResponse fail");
                }
                System.out.println("onResponse success");

                final String data = response.body().string();
                List<String> distincts = parseDistincts(data);
                if (distincts.size() != 0) {
                    String content = "";
                    for (String dis : distincts) {
                        content += dis;
                    }
                    FileUtil.writeToFile(FileUtil.getDistinctsFilePath(), content);
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
            System.out.println("parseDistincts success");
        } catch (ParserException e) {
            System.out.println("parseDistincts error");
        }
        return distincts;
    }
}