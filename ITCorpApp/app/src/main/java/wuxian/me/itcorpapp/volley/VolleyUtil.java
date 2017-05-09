package wuxian.me.itcorpapp.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by wuxian on 9/5/2017.
 */

public class VolleyUtil {

    static RequestQueue requestQueue;

    private VolleyUtil() {
    }

    //Call this !
    public static void init(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context,
                    new OkHttpStack(OkhttpProvider.getClient()));

        }
    }

    public static void sendRequest(Request request) {
        requestQueue.add(request);
    }

}
