package wuxian.me.itcorpapp.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by wuxian on 9/5/2017.
 */

public class VolleyUtil {
    private static final String TAG = "VolleyUtil";

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

    public static void testVolley() {
        Log.e(TAG, "send stringrequest ");
        VolleyUtil.sendRequest(new StringRequest("http://192.168.13.187:8081/home",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "receive response: " + response);
                    }
                }, null));
    }
}
