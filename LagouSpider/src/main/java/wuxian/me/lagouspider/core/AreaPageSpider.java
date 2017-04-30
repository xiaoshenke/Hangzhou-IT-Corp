package wuxian.me.lagouspider.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import okhttp3.*;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.framework.control.JobManager;
import wuxian.me.lagouspider.framework.control.JobProvider;
import wuxian.me.lagouspider.framework.control.MaybeBlockedException;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.save.CompanySaver;
import wuxian.me.lagouspider.util.Helper;

import java.util.ArrayList;
import java.util.List;

import static wuxian.me.lagouspider.Config.EnableSaveDB.ENABLE_SAVE_COMPANY_DB;
import static wuxian.me.lagouspider.Config.Spider.ENABLE_SPIDER_COMPANY_MAIN;
import static wuxian.me.lagouspider.Config.SpiderUrl.URL_LAGOU_JAVA;
import static wuxian.me.lagouspider.Config.SpiderUrl.URL_LAGOU_POSITION_JSON;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 7/4/2017.
 *
 * 拉勾的某个区域下的公司不止一页 这个就是第x页的数据
 *
 */
public class AreaPageSpider extends BaseLagouSpider {

    private Area area;
    private int pageIndex;
    private List<Company> companyList = new ArrayList<Company>();

    public AreaPageSpider(@NotNull Area area, @NotNull int pageNum) {
        super();
        this.area = area;
        this.pageIndex = pageNum;
    }

    protected Request buildRequest() {
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
        return request;
    }

    public int parseRealData(String data) {
        String body = data;

        try {
            parseCompanylist(body);
        } catch (ParserException e) {
            return BaseSpider.RET_PARSING_ERR;
        } catch (MaybeBlockedException e) {

            return BaseSpider.RET_MAYBE_BLOCK;
        }

        if (ENABLE_SAVE_COMPANY_DB) {
            for (Company company : companyList) {
                saveCompany(company);
            }
        }

        if (ENABLE_SPIDER_COMPANY_MAIN) {
            for (Company company : companyList) {
                IJob job = JobProvider.getJob();
                job.setRealRunnable((new CompanySpider(company.company_id, company.company_fullname)));
                JobManager.getInstance().putJob(job);
            }
        }

        return BaseSpider.RET_SUCCESS;
    }

    private void parseCompanylist(@NotNull String data) throws ParserException, MaybeBlockedException {
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
                companyList.add(parseCompany((JsonObject) array.get(i)));
            } catch (MaybeBlockedException e) {
                mayBlockNum++;
            }
        }

        //如果失败的次数和array的size一样 认为可能被屏蔽
        if (array.size() != 0 && mayBlockNum == array.size()) {
            throw new MaybeBlockedException();
        }
    }


    private Company parseCompany(@NotNull JsonObject object) throws MaybeBlockedException {
        long id = object.get("companyId").getAsLong();
        Company company = new Company(id);

        if (!object.get("companyFullName").isJsonNull()) {
            company.company_fullname = object.get("companyFullName").getAsString().trim();
        } else {
            throw new MaybeBlockedException();
        }
        if (!object.get("industryField").isJsonNull()) {
            company.industryField = object.get("industryField").getAsString().trim();
        } else {
            throw new MaybeBlockedException();
        }

        if (!object.get("companySize").isJsonNull()) {
            company.company_size = object.get("companySize").getAsString().trim();
        }
        company.area_id = area.area_id;
        return company;
    }

    private void saveCompany(@Nullable Company company) {
        if (company != null) {
            CompanySaver.getInstance().saveModel(company);
        }
    }

    public String name() {
        return "AreaPageSpider: {index: " +
                pageIndex + " ," + area.name() + "}";
    }

}
