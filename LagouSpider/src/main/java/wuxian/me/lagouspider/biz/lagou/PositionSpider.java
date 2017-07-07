package wuxian.me.lagouspider.biz.lagou;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.NotNull;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.model.lagou.Company;
import wuxian.me.lagouspider.model.lagou.LPosition;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.anti.MaybeBlockedException;

import static wuxian.me.lagouspider.biz.lagou.LagouConfig.SpiderUrl.URL_LAGOU_JAVA;

/**
 * Created by wuxian on 7/7/2017.
 */
public class PositionSpider extends BaseLagouSpider {

    private static final String URL = "https://www.lagou.com/jobs/positionAjax.json?px=default";

    private static final String POSITION_TYPE = "Java";

    private static final String CITY = "杭州";

    private String area;
    private String distinc;
    private int pageNum;

    public PositionSpider(String distinct, int pageNum) {
        this.distinc = distinct;
        this.pageNum = pageNum;
    }

    protected Request buildRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL)
                .newBuilder();
        urlBuilder.addQueryParameter("city", CITY)
                .addQueryParameter("district", distinc)
                .addQueryParameter("needAddtionalResult", "false");

        final String referer = HttpUrl.parse(URL_LAGOU_JAVA).newBuilder()
                .addQueryParameter("city", "杭州")
                .addQueryParameter("district", distinc)
                .build().toString();

        FormBody.Builder bodyBuilder = new FormBody.Builder();

        if (pageNum == 1) {
            bodyBuilder.add("first", "true");
        } else {
            bodyBuilder.add("first", "false");
        }
        bodyBuilder.add("pn", String.valueOf(pageNum));
        bodyBuilder.add("kd", POSITION_TYPE);

        Request request = new Request.Builder()
                .headers(Helper.getLagouHeader(referer, LagouConfig.SPIDER_NAME))
                .url(urlBuilder.build().toString())
                .post(bodyBuilder.build())
                .build();
        return request;
    }

    private void parsePositionlist(@NotNull String data) throws ParserException, MaybeBlockedException {
        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(data);

        if (obj.get("content").isJsonNull()) {
            throw new MaybeBlockedException();
        }
        obj = obj.getAsJsonObject("content");

        if (obj.get("positionResult").isJsonNull()) {
            throw new MaybeBlockedException();
        }
        obj = obj.getAsJsonObject("positionResult");

        if (obj.get("result").isJsonNull()) {
            throw new MaybeBlockedException();
        }
        JsonArray array = obj.getAsJsonArray("result");

        int mayBlockNum = 0;
        for (int i = 0; i < array.size(); i++) {
            try {
                LPosition position = parsePosition((JsonObject) array.get(i));

                if(position != null) {
                    LogManager.info(position.toString());
                }
            } catch (MaybeBlockedException e) {
                mayBlockNum++;
            }
        }

        //如果失败的次数和array的size一样 认为可能被屏蔽
        if (array.size() != 0 && mayBlockNum == array.size()) {
            throw new MaybeBlockedException();
        }
    }

    private LPosition parsePosition(@NotNull JsonObject object) throws MaybeBlockedException {

        Gson gson = new Gson();

        return gson.fromJson(object, LPosition.class);

    }


    public int parseRealData(String data) {
        String body = data;
        LogManager.info(data);

        try {
            parsePositionlist(body);
        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        } catch (MaybeBlockedException e) {

            return BaseSpider.RET_MAYBE_BLOCK;
        }

        return BaseSpider.RET_SUCCESS;
    }

    public String name() {
        return "PositionSpider:{"  + " distinct:"
                + distinc + " page:" + pageNum + "}";
    }
}
