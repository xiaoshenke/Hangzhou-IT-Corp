package wuxian.me.itcorpapp;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wuxian.me.itcorpapp.base.BaseActionbarActivity;
import wuxian.me.itcorpapp.map.MapLoaderHelper;
import wuxian.me.itcorpapp.map.MarkerUtil;
import wuxian.me.itcorpapp.map.OnMapLocatedListener;
import wuxian.me.itcorpapp.model.Company;

/**
 * @AMap:实际一个地图的控制器
 */
public class MainActivity extends BaseActionbarActivity implements OnMapLocatedListener {

    private static String TAG = "Main";
    private MapLoaderHelper mMapLoader;

    @BindView(R.id.map)
    MapView mapView;

    //MapView可以通过new MapView(context)出来。
    // 然后mAMap = mapView.getMap();因此这里可以做一个提前load。
    private AMap mAMap;

    private boolean mMapLocationed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isRequestingPermission()){
            return;
        }

        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mAMap = mapView.getMap();
        mMapLoader = new MapLoaderHelper(this, mAMap, this);

        loadMap();
    }

    @Override
    public void onMapLocated(Location location) {
        mMapLocationed = true;

        Log.e(TAG, "location: " + location.getLongitude() + " ," + location.getLatitude());

        doMapDrawing();
    }

    private void loadMap() {
        mMapLoader.loadMap();
    }

    private void doMapDrawing() {
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.e(TAG, "onCameraChange, " + cameraPosition.toString());

                if (cameraPosition.zoom > 11) {
                    mAMap.clear();
                }
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

            }
        });

        Company company = new Company();
        company.company_id = 749;
        company.name = "支付宝（中国）网络技术有限公司";
        company.logo = "http://www.lgstatic.com/thumbnail_300x300/image1/M00/00/04/CgYXBlTUV_6AdvqVAAApge7s8yA551.jpg";
        company.financeStage = "D轮及以上";
        company.webLink = "http://www.alipay.com";
        company.description = "支付宝（中国）网络技术有限公司是国内领先的第三方支付平台，2014年成为当前全球最大的移动支付厂商";
        //company.longitude = 120.125809;
        //company.lantitude = 30.272553;
        //company.location = "杭州西湖区江省杭州市黄龙时代广场B座";

        wuxian.me.itcorpapp.model.Location location = new wuxian.me.itcorpapp.model.Location();
        location.lantitude = String.valueOf(30.272553);
        location.longitude = String.valueOf(120.125809);

        List<wuxian.me.itcorpapp.model.Location> locations = new ArrayList<>();
        locations.add(location);
        company.locationList = locations;

        MarkerUtil.addMarker(mAMap, company);


    }

    protected List<String> getRequestPermission(){
        List<String> list = super.getRequestPermission();
        if(!list.contains(Manifest.permission.ACCESS_COARSE_LOCATION)){
            list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        return list;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mMapLoader.onDestroy();
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

}
