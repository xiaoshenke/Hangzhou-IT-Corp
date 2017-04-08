package wuxian.me.lagouspider.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import okhttp3.*;
import wuxian.me.lagouspider.Main;
import wuxian.me.lagouspider.control.Fail;
import wuxian.me.lagouspider.control.JobMonitor;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.OkhttpProvider;

import java.io.IOException;

import static wuxian.me.lagouspider.Config.URL_LAGOU_JAVA;
import static wuxian.me.lagouspider.Config.URL_LAGOU_POSITION_JSON;

/**
 * Created by wuxian on 7/4/2017.
 */
public class AreaPageSpider implements Runnable {

    private Area area;
    private int pageIndex;

    public AreaPageSpider(@NotNull Area area, @NotNull int pageNum) {
        this.area = area;
        this.pageIndex = pageNum;
    }

    public void run() {
        beginSpider();
    }


    private void beginSpider() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LAGOU_POSITION_JSON)
                .newBuilder();
        urlBuilder.addQueryParameter("city", "杭州")
                .addQueryParameter("district", area.distinct_name)
                .addQueryParameter("bizArea", area.name);

        final String referer = HttpUrl.parse(URL_LAGOU_JAVA).newBuilder()
                .addQueryParameter("city", "杭州")
                .addQueryParameter("district", area.distinct_name)
                .addQueryParameter("bizArea", area.name)
                .build().toString();

        FormBody.Builder bodyBuilder = new FormBody.Builder();

        if (pageIndex == 1) {
            bodyBuilder.add("first", "true");
        } else {
            bodyBuilder.add("first", "false");
        }
        bodyBuilder.add("pn", String.valueOf(pageIndex));
        bodyBuilder.add("kd", "Java");

        Request request = new Request.Builder()
                .headers(Helper.getHeaderBySpecifyRef(referer))
                .url(urlBuilder.build().toString())
                .post(bodyBuilder.build())
                .build();

        OkhttpProvider.getClient().newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                JobMonitor.getInstance().fail(AreaPageSpider.this, new Fail(Fail.FAIL_NETWORK_ERROR));
                Main.logger.error("AreaPageSpider onFailure");
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    JobMonitor.getInstance().fail(AreaPageSpider.this, new Fail(response.code(), response.message()));
                    Main.logger.error("AreaPageSpider onSuccess,error code" + response.code());
                    return;
                } else {
                    JobMonitor.getInstance().success(AreaPageSpider.this);
                    Main.logger.info("AreaPageSpider onSuccess");
                }

                parseResult(response.body().string());
            }
        });

    }

    //Todo:存的时候多一层缓存？
    private void saveCompany(@Nullable Company company) {
        if (company != null) {
            System.out.println(company.toString());
        }
    }

    private Company getCompany(@NotNull JsonObject object) {
        long id = object.get("companyId").getAsLong();
        Company company = new Company(id);
        company.company_fullname = object.get("companyFullName").getAsString();
        company.financeStage = object.get("financeStage").getAsString();
        company.industryField = object.get("industryField").getAsString();
        company.area_id = area.area_id;
        return company;
    }

    private void parseResult(@NotNull String data) {
        Main.logger.debug("begin to parse json Result");
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(data);
            obj = obj.getAsJsonObject("content");
            obj = obj.getAsJsonObject("positionResult");

            JsonArray array = obj.getAsJsonArray("result");
            for (int i = 0; i < array.size(); i++) {
                saveCompany(getCompany((JsonObject) array.get(i)));
            }

        } catch (JsonIOException e) {
            Main.logger.error("AreaPageSpider parse json result fail");
        }

    }

}
