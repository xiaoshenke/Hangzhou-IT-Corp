package wuxian.me.itcorpapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @AMap:实际一个地图的控制器 Todo: map从初始化到location成功可以封装一下
 */
public class MainActivity extends BaseActionbarActivity implements LocationSource, OnMapFirstLocatedListener {
    private static final int REQUEST_PERMISSIONS = 101;

    @BindView(R.id.map)
    MapView mapView;

    private LocationSource.OnLocationChangedListener mLocChangeListener;
    private AMapLocationClient mLocationClient = null;  //used to locate

    //MapView可以通过new MapView(context)出来。然后mAMap = mapView.getMap();因此这里可以做一个提前load。
    private AMap mAMap;

    private boolean mMapLocationed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mAMap = mapView.getMap();

        if (checkPermission()) {
            doLoadMap();
        }
    }

    //Todo:real bussiness
    private void doMapDrawing() {
        ;
    }

    @Override
    public void onMapFirstLocatedListener(Location location) {
        mLocationClient.stopLocation();      //只定位一次
        if (mLocChangeListener != null) {
            //调用这行代码使得高德地图定位到杭州 否则一直在北京
            mLocChangeListener.onLocationChanged((AMapLocation) location);
        }

        mMapLocationed = true;
        doMapDrawing();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);
            }

            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doLoadMap();
                } else {
                }
                return;
            }
        }
    }

    private void doLoadMap() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected View getSubview() {
        View view = inflate(R.layout.activity_main);
        return view;
    }

    @Override
    protected boolean useCustomToolbar() {
        return false;
    }

    @NonNull
    @Override
    protected String pageTitle() {
        return "ITCorpApp";
    }

    @Nullable
    @Override
    protected List<MenuItemData> getMenuItemDatas() {
        return null;
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

}
