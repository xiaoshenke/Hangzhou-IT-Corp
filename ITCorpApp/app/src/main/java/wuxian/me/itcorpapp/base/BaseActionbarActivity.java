package wuxian.me.itcorpapp.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import wuxian.me.andbootstrap.BaseActivity;
import wuxian.me.itcorpapp.R;

/**
 * Created by wuxian on 4/3/2017.
 */

public abstract class BaseActionbarActivity extends BaseActivity {
    private static final int REQUEST_PERMISSIONS = 101;
    protected Toolbar mToolbar;
    private View mRoot;

    private List<String> permissinList;
    private List<String> reqList;

    @CallSuper
    @NonNull
    protected List<String> getRequestPermission() {
        return new ArrayList<>();
    }

    private boolean checkPermissions() {
        boolean ret = true;
        reqList = new ArrayList<>();
        for (String request : permissinList) {
            if (ContextCompat.checkSelfPermission(this, request)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        request)) {
                } else {
                    reqList.add(request);
                }

            }
        }

        if (reqList.size() != 0) {
            ActivityCompat.requestPermissions(this,
                    reqList.toArray(new String[reqList.size()]), REQUEST_PERMISSIONS);
            ret = false;
        }

        return ret;
    }

    private boolean isRequesting = false;

    protected final boolean isRequestingPermission() {
        return isRequesting;
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length == reqList.size()) {
                    boolean success = true;
                    for(int i=0;i < grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            success = false;
                            break;
                        }
                    }
                    if(success){
                        isRequesting = false;
                        recreate();

                        break;
                    }

                }
                checkPermissions();  //重新进行下一轮申请
                return;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissinList = getRequestPermission();
        if (permissinList.size() != 0) {
            if (!checkPermissions()) {
                isRequesting = true;
            }
        }

        mRoot = LayoutInflater.from(this).inflate(R.layout.view_base_actionbar, null);
        setContentView(mRoot);
        mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);

        mToolbar.setTitle(pageTitle());

        initMenu(mToolbar.getMenu());
        if (useCustomToolbar()) {
            customToolbar(mToolbar);
        }
        setSupportActionBar(mToolbar);   //set title什么的必须在setSupportActionBar之前
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        FrameLayout container = (FrameLayout) mRoot.findViewById(R.id.container);
        container.addView(getSubview());

        return;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        initMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void initMenu(Menu menu) {
        menu.clear();
        List<MenuItemData> menus = getMenuItemDatas();
        if (menus != null && menus.size() != 0) {
            for (MenuItemData menuItemData : menus) {
                MenuItem item = menu.add(0, menuItemData.itemId, menuItemData.itemId, menuItemData.title);
                item.setOnMenuItemClickListener(menuItemData.onClickListener);
                if (menuItemData.iconRes > 0) {
                    item.setIcon(menuItemData.iconRes);
                }
            }
        }
    }

    //子类的view应该在这里控制
    protected abstract View getSubview();

    protected abstract boolean useCustomToolbar();

    protected void customToolbar(Toolbar toolbar) {
    }

    @NonNull
    protected abstract String pageTitle();

    @Nullable
    protected abstract List<MenuItemData> getMenuItemDatas();

    public static class MenuItemData {
        public int itemId;
        public String title;
        public int iconRes;
        public boolean atTitle = false;
        public MenuItem.OnMenuItemClickListener onClickListener;
    }
}
