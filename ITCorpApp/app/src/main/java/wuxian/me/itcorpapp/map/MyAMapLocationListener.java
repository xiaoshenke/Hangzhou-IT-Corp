package wuxian.me.itcorpapp.map;

import android.support.annotation.NonNull;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by wuxian on 3/5/2017.
 */

public class MyAMapLocationListener implements AMapLocationListener {
    private OnMapLocatedListener listener;

    public MyAMapLocationListener(@NonNull OnMapLocatedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                listener.onMapLocated(amapLocation);
            } else {
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

    }
}
