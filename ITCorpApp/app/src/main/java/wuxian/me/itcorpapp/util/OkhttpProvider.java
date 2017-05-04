package wuxian.me.itcorpapp.util;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by wuxian on 1/4/2017.
 * <p>
 * usage: OkHttpClient client = OkhttpProvider.getClent();
 */
public class OkhttpProvider {

    private static OkHttpClient client;

    private OkhttpProvider() {
    }

    public static OkHttpClient getClient() {

        if (client == null) {
            client = new OkHttpClient.Builder().readTimeout(
                    1000 * 10,
                    TimeUnit.MILLISECONDS).build();
        }
        return client;
    }
}
