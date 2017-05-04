package wuxian.me.itcorpapp.map;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.MyLocationStyle;

/**
 * Created by wuxian on 4/5/2017.
 */

public class MapLoaderHelper implements LocationSource, OnMapLocatedListener {

    private Context mContext;
    private AMap mAMap;
    private OnMapLocatedListener listener;

    private LocationSource.OnLocationChangedListener mLocChangeListener;
    private AMapLocationClient mLocationClient = null;  //used to locate

    public MapLoaderHelper(@NonNull Context context, @NonNull AMap amap, @NonNull OnMapLocatedListener listener) {
        this.mContext = context;
        this.mAMap = amap;
        this.listener = listener;
    }

    public void loadMap() {
        mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
        mLocationClient.setLocationListener(new MyAMapLocationListener(this));

        mLocationClient.setLocationOption(getLocationOption());
        mAMap.setLocationSource(this);
        mAMap.setMyLocationStyle(getLocationStyle());
        mAMap.setMyLocationEnabled(true);
    }

    private MyLocationStyle getLocationStyle() {
        return new MyLocationStyle();
    }

    private AMapLocationClientOption getLocationOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setInterval(100);
        option.setWifiActiveScan(true);
        option.setNeedAddress(true);
        option.setHttpTimeOut(20000);
        option.setLocationCacheEnable(false);
        return option;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocChangeListener = onLocationChangedListener;
        mLocationClient.startLocation();
    }

    @Override
    public void deactivate() {  //// FIXME: 3/5/2017 什么时候会调这个函数
        mLocChangeListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onMapLocated(Location location) {
        mLocationClient.stopLocation();      //只定位一次
        if (mLocChangeListener != null) {
            //调用这行代码使得高德地图定位到杭州 否则一直在北京
            mLocChangeListener.onLocationChanged((AMapLocation) location);
        }

        listener.onMapLocated(location);
    }

    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }
}
