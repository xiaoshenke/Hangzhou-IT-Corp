package wuxian.me.itcorpapp.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import wuxian.me.andbootstrap.BaseActivity;
import wuxian.me.itcorpapp.R;

/**
 * Created by wuxian on 4/3/2017.
 */

public abstract class BaseActionbarActivity extends BaseActivity {
    protected Toolbar mToolbar;
    private View mRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
