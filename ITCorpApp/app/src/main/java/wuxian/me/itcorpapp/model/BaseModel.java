package wuxian.me.itcorpapp.model;

import com.amap.api.maps2d.model.LatLng;

import java.util.List;

/**
 * Created by wuxian on 5/5/2017.
 * <p>
 * 用于高德API的@Marker显示用
 * <p>
 * Todo: To be finished
 */

public abstract class BaseModel {

    public static final int TYPE_MARKER = 0;
    public static final int TYPE_INFOWINDOW = 1;

    public int getDisplayType() {
        return TYPE_MARKER;
    }

    public abstract int getDisplayLevel();

    public abstract String getTitle();

    public abstract String getSnippet();

    public abstract List<LatLng> getLatLngs();

    //显示规则 若能下载到远程的uri 则显示远程的,否则显示@getTitle的图案
    public abstract String getIconUri();


}
