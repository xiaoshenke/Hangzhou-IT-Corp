package wuxian.me.itcorpapp.util;

import android.content.Context;

/**
 * Created by wuxian on 5/5/2017.
 */

public class Helper {
    private static Context sContext;

    public static float density;

    /**
     * Should be called First !
     */
    public static void init(Context context) {
        sContext = context;
        density = sContext.getResources().getDisplayMetrics().density;

    }

    public final static int dp2px(float value) {
        return (int) Math.ceil(density * value);
    }

    private Helper() {
        ;
    }
}
