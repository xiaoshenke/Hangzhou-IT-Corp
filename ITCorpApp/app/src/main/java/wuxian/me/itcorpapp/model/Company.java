package wuxian.me.itcorpapp.model;

import android.support.annotation.Nullable;

import com.amap.api.maps2d.model.LatLng;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

import wuxian.me.itcorpapp.util.VisibleOption;
import wuxian.me.itcorpapp.util.VisibleUtil;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wuxian on 5/5/2017.
 */

@Entity
public class Company extends BaseModel {

    @Id
    @Property(nameInDb = "company_id")
    public long company_id;

    @Property(nameInDb = "short_name")
    public String shortName;

    @Property(nameInDb = "full_name")
    public String name;

    @Property(nameInDb = "industry_field")
    public String industryField;  //公司主营业务

    @Property(nameInDb = "company_size")
    public String companySize;

    @Property(nameInDb = "company_logo")
    public String logo;           //logo uri

    @Transient
    public List<Location> locationList;

    @Property(nameInDb = "finance_stage")
    public int stage;

    @Property(nameInDb = "company_weblink")
    public String webLink;       //公司主页

    @Property(nameInDb = "company_des")
    public String description;//拉勾上的公司自我描述

    @Transient
    public int score;

    @Property(nameInDb = "finance_score")
    public int finaceScore;

    @Transient
    //Fixme: 应该是个boolean值
    public int authenScore;

    @Property(nameInDb = "position_score")
    public int positionNumScore;

    @Property(nameInDb = "interview_score")
    public int interScore;

    @Generated(hash = 723163711)
    public Company(long company_id, String shortName, String name,
                   String industryField, String companySize, String logo, int stage,
                   String webLink, String description, int finaceScore,
                   int positionNumScore, int interScore) {
        this.company_id = company_id;
        this.shortName = shortName;
        this.name = name;
        this.industryField = industryField;
        this.companySize = companySize;
        this.logo = logo;
        this.stage = stage;
        this.webLink = webLink;
        this.description = description;
        this.finaceScore = finaceScore;
        this.positionNumScore = positionNumScore;
        this.interScore = interScore;
    }

    @Generated(hash = 1096856789)
    public Company() {
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return industryField;
    }

    @Override
    public List<LatLng> getLatLngs() {

        if (locationList != null && locationList.size() != 0) {
            List<LatLng> list = new ArrayList<>();
            for (Location location : locationList) {
                list.add(new LatLng(Double.parseDouble(location.lantitude),
                        Double.parseDouble(location.longitude)));
            }
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public float getZIndex() {
        return VisibleUtil.getZIndexOf(this);
    }

    @Override
    public String getIconUri() {
        return logo;
    }

    //SHOULD BE CALCULATED EVERYTIME!
    public boolean isVisibleWithOption(@Nullable VisibleOption option) {
        if (option == null) {
            option = VisibleOption.DEFAULT;
        }
        return VisibleUtil.isVisible(this, option);
    }

    public long getCompany_id() {
        return this.company_id;
    }

    public void setCompany_id(long company_id) {
        this.company_id = company_id;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustryField() {
        return this.industryField;
    }

    public void setIndustryField(String industryField) {
        this.industryField = industryField;
    }

    public String getCompanySize() {
        return this.companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getWebLink() {
        return this.webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFinaceScore() {
        return this.finaceScore;
    }

    public void setFinaceScore(int finaceScore) {
        this.finaceScore = finaceScore;
    }

    public int getPositionNumScore() {
        return this.positionNumScore;
    }

    public void setPositionNumScore(int positionNumScore) {
        this.positionNumScore = positionNumScore;
    }

    public int getInterScore() {
        return this.interScore;
    }

    public void setInterScore(int interScore) {
        this.interScore = interScore;
    }


}
