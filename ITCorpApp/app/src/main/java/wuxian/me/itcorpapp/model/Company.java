package wuxian.me.itcorpapp.model;

import com.amap.api.maps2d.model.LatLng;

/**
 * Created by wuxian on 5/5/2017.
 */

public class Company extends BaseModel {

    //public int area_id = -1;
    public long company_id;
    public String shortName;

    public String name;

    public String industryField;  //公司主营业务
    public String companySize;

    public String logo;           //logo uri
    public String location;
    public double longitude;     //经度
    public double lantitude;        //纬度

    public String financeStage;  //公司融资轮次
    public String webLink;       //公司主页

    public String description;//拉勾上的公司自我描述

    public float score;      //

    //Todo:根据不同的display level作出不同的显示
    //比如说在marker不同的z序,显示的图片的大小 或者给不同的颜色？？
    @Override
    public int getDisplayLevel() {
        return Display.LEVEL_1;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return financeStage;
    }

    @Override
    public LatLng getLatLng() {
        return new LatLng(lantitude, longitude);
    }

    @Override
    public String getIconUri() {
        return logo;
    }
}
