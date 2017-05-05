package wuxian.me.itcorpapp;

import android.app.Application;

import wuxian.me.itcorpapp.map.MarkerUtil;
import wuxian.me.itcorpapp.util.Helper;

/**
 * Created by wuxian on 4/5/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Helper.init(this);
        MarkerUtil.init(this);
    }
}
