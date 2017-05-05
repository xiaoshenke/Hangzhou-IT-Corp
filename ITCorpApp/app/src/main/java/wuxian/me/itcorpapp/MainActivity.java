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

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wuxian.me.itcorpapp.base.BaseActionbarActivity;
import wuxian.me.itcorpapp.map.MapLoaderHelper;
import wuxian.me.itcorpapp.map.OnMapLocatedListener;

/**
 * @AMap:实际一个地图的控制器
 */
public class MainActivity extends BaseActionbarActivity implements OnMapLocatedListener {
    private static final int REQUEST_PERMISSIONS = 101;

    private MapLoaderHelper mMapLoader;

    @BindView(R.id.map)
    MapView mapView;

    //MapView可以通过new MapView(context)出来。然后mAMap = mapView.getMap();因此这里可以做一个提前load。
    private AMap mAMap;

    private boolean mMapLocationed = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mAMap = mapView.getMap();

        mMapLoader = new MapLoaderHelper(this, mAMap, this);

        if (checkPermission()) {
            doLoadMap();
        }
    }

    @Override
    public void onMapLocated(Location location) {
        mMapLocationed = true;

        doMapDrawing();
    }

    private void doLoadMap() {
        mMapLoader.loadMap();
    }

    //Todo:real bussiness
    private void doMapDrawing() {
        //Picasso.with(this).load().
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
