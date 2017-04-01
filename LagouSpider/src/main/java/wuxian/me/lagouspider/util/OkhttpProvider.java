package wuxian.me.lagouspider.util;

import okhttp3.OkHttpClient;

/**
 * Created by wuxian on 1/4/2017.
 */
public class OkhttpProvider {

    private static OkHttpClient client = new OkHttpClient();

    private OkhttpProvider() {
    }

    public static OkHttpClient getClient() {
        return client;
    }
}
