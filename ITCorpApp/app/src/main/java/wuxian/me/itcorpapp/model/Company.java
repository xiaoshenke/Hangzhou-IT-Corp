package wuxian.me.itcorpapp.model;

import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 5/5/2017.
 */

public class Company extends BaseModel {

    public long company_id;
    public String shortName;

    public String name;

    public String industryField;  //公司主营业务
    public String companySize;

    public String logo;           //logo uri

    public List<Location> locationList;

    public double longitude;     //经度
    public double lantitude;        //纬度

    public String financeStage;  //公司融资轮次
    public String webLink;       //公司主页

    public String description;//拉勾上的公司自我描述

    public int score;

    public int finaceScore;

    public int authenScore;

    public int positionNumScore;

    public int interScore;

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
    public String getIconUri() {
        return logo;
    }
}
