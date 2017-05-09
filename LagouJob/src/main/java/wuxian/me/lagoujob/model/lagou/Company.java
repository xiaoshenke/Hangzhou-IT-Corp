package wuxian.me.lagoujob.model.lagou;

import com.google.gson.annotations.Expose;
import org.springframework.stereotype.Service;
import wuxian.me.lagoujob.model.BaseModel;

import java.util.List;

/**
 * Created by wuxian on 1/5/2017.
 * <p>
 * 应该具有的属性：位置,geo版的位置,公司名称,公司id,公司轮次,公司的业务等等
 */
public class Company extends BaseModel {

    public long company_id;

    public String shortName;
    public String name;

    public String industryField;  //公司主营业务
    public String companySize;

    public String logo;           //logo uri

    public List<Location> locationList;

    public String financeStage;  //公司融资轮次
    public String webLink;       //公司主页

    public String description;//拉勾上的公司自我描述

    @Expose(serialize = false)
    public String lagouAuthentic;

    @Expose(serialize = false)
    public String positionNum;

    @Expose(serialize = false)
    public String interviewScore;       //面试评价评分

    public int score;

    public int finaceScore;

    public int authenScore;

    public int positionNumScore;

    public int interScore;

    public String name() {
        return "Company: {id: " + company_id + " name: " + name + "}";
    }

}
