package wuxian.me.itcorpapp.util;

import android.content.Context;

/**
 * Created by wuxian on 5/5/2017.
 */

public class Helper {
    private static Context sContext;

    public static float density;
    private static float scaleDensity;

    /**
     * Should be called First !
     */
    public static void init(Context context) {
        sContext = context;
        density = sContext.getResources().getDisplayMetrics().density;
        scaleDensity = sContext.getResources().getDisplayMetrics().scaledDensity;

    }

    public final static int dp2px(float value) {
        return (int) Math.ceil(density * value);
    }

    public final static int sp2px(float value) {
        return (int) Math.ceil(scaleDensity * value + 0.5f);
    }

    public static int getColor(int resId) {
        return sContext.getResources().getColor(resId);
    }

    private Helper() {
        ;
    }
}
