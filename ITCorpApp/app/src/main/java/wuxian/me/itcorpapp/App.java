package wuxian.me.itcorpapp;

import android.app.Application;

import wuxian.me.itcorpapp.greendao.GreenDaoHelper;
import wuxian.me.itcorpapp.map.MarkerUtil;
import wuxian.me.itcorpapp.util.Helper;
import wuxian.me.itcorpapp.volley.VolleyUtil;

/**
 * Created by wuxian on 4/5/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Helper.init(this);
        MarkerUtil.init(this);
        VolleyUtil.init(this);
        GreenDaoHelper.init(this);
    }
}
