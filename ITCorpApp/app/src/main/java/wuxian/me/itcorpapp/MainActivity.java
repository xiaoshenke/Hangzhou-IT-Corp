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
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wuxian.me.itcorpapp.api.Api;
import wuxian.me.itcorpapp.base.BaseActionbarActivity;
import wuxian.me.itcorpapp.greendao.GreenDaoHelper;
import wuxian.me.itcorpapp.map.MapLoaderHelper;
import wuxian.me.itcorpapp.map.MarkerUtil;
import wuxian.me.itcorpapp.map.OnMapLocatedListener;
import wuxian.me.itcorpapp.model.Company;
import wuxian.me.itcorpapp.model.LocationDao;
import wuxian.me.itcorpapp.util.VisibleOption;
import wuxian.me.itcorpapp.volley.GsonRequest;
import wuxian.me.itcorpapp.volley.VolleyUtil;

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
    private boolean mDataloaded = false;

    //Todo:网络请求 然后存入dao
    private List<Company> companyList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isRequestingPermission()) {
            return;
        }

        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mAMap = mapView.getMap();
        mMapLoader = new MapLoaderHelper(this, mAMap, this);

        mMapLoader.loadMap();

        loadData();
    }

    private void loadData() {
        companyList = GreenDaoHelper.companyDao().loadAll();
        if (companyList == null || companyList.size() == 0) {
            VolleyUtil.sendRequest(new GsonRequest(Api.LIST,
                    new TypeToken<List<Company>>() {
                    },
                    new Response.Listener<List<Company>>() {
                        @Override
                        public void onResponse(List<Company> response) {
                            for (Company company : response) {
                                GreenDaoHelper.companyDao().insert(company);

                                if (company.locationList != null) {
                                    for (wuxian.me.itcorpapp.model.Location location : company.locationList) {
                                        GreenDaoHelper.locationDao().insert(location);
                                    }
                                }
                            }
                            companyList = response;
                            mDataloaded = true;
                            maybeDisplay();

                        }
                    }, null));
        } else {

            for (Company company : companyList) {
                List<wuxian.me.itcorpapp.model.Location> locationList =
                        getLocationQuery(company.company_id).list();
                company.locationList = locationList;
            }

            mDataloaded = true;
            maybeDisplay();
        }
    }

    //http://greenrobot.org/greendao/documentation/queries/
    private QueryBuilder getLocationQuery(long companyId) {
        return GreenDaoHelper.locationDao().queryBuilder()
                .where(LocationDao.Properties.Company_id.eq(companyId));
    }

    private void maybeDisplay() {
        if (mMapLocationed && mDataloaded) {
            mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    Log.i(TAG, "onCameraChange, " + cameraPosition.toString());
                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    //Todo: 优化 不应该每次变化都reRender,cpu消耗太高
                    drawEveryFrameWith(cameraPosition);
                }
            });
            drawEveryFrameWith(mAMap.getCameraPosition());
        }
    }

    @Override
    public void onMapLocated(Location location) {
        mMapLocationed = true;
        maybeDisplay();
        Log.i(TAG, "location: " + location.getLongitude() + " ," + location.getLatitude());
    }

    private void drawEveryFrameWith(@NonNull CameraPosition position) {
        mAMap.clear(); //先清除

        VisibleOption visibleOption = new VisibleOption();
        visibleOption.cameraZoom = position.zoom;
        for (Company company : companyList) {

            if (company.isVisibleWithOption(visibleOption)) {  //本身判定自己可见 那么显示
                MarkerUtil.addMarker(mAMap, company);
            }
        }
    }

    private void doMapDrawing() {
        /*
        Company company = new Company();
        company.company_id = 749;
        company.name = "支付宝（中国）网络技术有限公司";
        company.logo = "http://www.lgstatic.com/thumbnail_300x300/image1/M00/00/04/CgYXBlTUV_6AdvqVAAApge7s8yA551.jpg";
        company.financeStage = Finance.STAGE_D_OR_PLUS.getValue();
        company.webLink = "http://www.alipay.com";
        company.description = "支付宝（中国）网络技术有限公司是国内领先的第三方支付平台，2014年成为当前全球最大的移动支付厂商";

        wuxian.me.itcorpapp.model.Location location = new wuxian.me.itcorpapp.model.Location();
        location.lantitude = String.valueOf(30.272553);
        location.longitude = String.valueOf(120.125809);

        List<wuxian.me.itcorpapp.model.Location> locations = new ArrayList<>();
        locations.add(location);
        company.locationList = locations;

        MarkerUtil.addMarker(mAMap, company);
        */
    }

    protected List<String> getRequestPermission() {
        List<String> list = super.getRequestPermission();
        if (!list.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
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
