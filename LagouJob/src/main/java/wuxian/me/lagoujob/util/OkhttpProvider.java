package wuxian.me.lagoujob.util;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by wuxian on 2/5/2017.
 */
public class OkhttpProvider {

    private static OkHttpClient client;

    private OkhttpProvider() {
    }

    public static OkHttpClient getClient() {
        if (client == null) {
            client = (new OkHttpClient.Builder()).readTimeout(1000 * 10, TimeUnit.MILLISECONDS).build();
        }

        return client;
    }
}
