package wuxian.me.itcorpapp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by wuxian on 3/5/2017.
 */

public class MyAMapLocationListener implements AMapLocationListener {
    private OnMapFirstLocatedListener listener;

    MyAMapLocationListener(@NonNull OnMapFirstLocatedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                listener.onMapFirstLocatedListener(amapLocation);
            } else {
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

    }
}
