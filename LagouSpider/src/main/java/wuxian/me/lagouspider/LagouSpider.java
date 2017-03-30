package wuxian.me.lagouspider;

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

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * Todo: integrate logging
 */
public class LagouSpider {
    private static final String URL_LAGOU_HANGZHOU_JAVA = "https://www.lagou.com/zhaopin/Java/?labelWords=label";
    private static final String CUT = ",";

    private final OkHttpClient client = new OkHttpClient();

    private Headers distinctsHeaders;

    public LagouSpider() {
        Headers.Builder disinctsBuilder = new Headers.Builder();
        disinctsBuilder.add("Connection", "keep_alive");
        disinctsBuilder.add("Host", "www.lagou.com");
        disinctsBuilder.add("Referer", "https://www.lagou.com/");
        disinctsBuilder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        distinctsHeaders = disinctsBuilder.build();
    }


    public void beginSpider() {
        if (!distinctsFileValid()) {
            getDisticts();
        }
    }

    private boolean distinctsFileValid() {
        return FileUtil.checkFileExist(FileUtil.getDistinctsFilePath());
    }

    private void getDisticts() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_HANGZHOU_JAVA).newBuilder();

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
                            if (j == 0) {
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
