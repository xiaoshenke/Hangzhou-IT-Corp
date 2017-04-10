package wuxian.me.lagouspider.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import okhttp3.*;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.control.Fail;
import wuxian.me.lagouspider.control.JobMonitor;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.save.CompanySaver;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.OkhttpProvider;
import java.io.IOException;
import static wuxian.me.lagouspider.Config.URL_LAGOU_JAVA;
import static wuxian.me.lagouspider.Config.URL_LAGOU_POSITION_JSON;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 7/4/2017.
 */
public class AreaPageSpider extends BaseLagouSpider {

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
                JobMonitor.getInstance().fail(AreaPageSpider.this, Fail.NETWORK_ERR);
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    JobMonitor.getInstance().fail(AreaPageSpider.this, new Fail(response.code(), response.message()));
                    return;
                } else {
                    JobMonitor.getInstance().success(AreaPageSpider.this);
                }
                parseResult(response.body().string());
            }
        });

    }

    private void saveCompany(@Nullable Company company) {

        if (!Config.ENABLE_SAVE_COMPANY_DB) {
            logger().info("SaveCompany " + company.toString());
            return;
        }
        if (company != null) {
            CompanySaver.getInstance().saveCompany(company);
        }
    }

    private Company getCompany(@NotNull JsonObject object) {
        long id = object.get("companyId").getAsLong();
        Company company = new Company(id);

        if (object.get("companyFullName") != null) {
            company.company_fullname = object.get("companyFullName").getAsString();
        }
        if (object.get("financeStage") != null) {
            company.financeStage = object.get("financeStage").getAsString();
        }
        if (object.get("industryField") != null) {
            company.industryField = object.get("industryField").getAsString();
        }
        company.area_id = area.area_id;
        return company;
    }

    private void parseResult(@NotNull String data) {
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
            logger().error("AreaPageSpider parse json result fail");
        }

    }

    @Override
    public String toString() {
        return "AreaPageSpider: pageIndex is " + pageIndex + " area is " + area.toString();
    }

    public String simpleName() {
        return "AreaPageSpider index:" + pageIndex + " " + area.toString();
    }
}
