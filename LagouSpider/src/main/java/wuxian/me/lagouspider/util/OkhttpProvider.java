package wuxian.me.lagouspider.util;

import okhttp3.OkHttpClient;
import wuxian.me.lagouspider.Config;

import java.util.concurrent.TimeUnit;

/**
 * Created by wuxian on 1/4/2017.
 */
public class OkhttpProvider {

    private static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder().readTimeout(Config.SOCKET_READ_TIMEOUT, TimeUnit.MICROSECONDS).build();
    }

    private OkhttpProvider() {
    }

    public static OkHttpClient getClient() {
        return client;
    }
}
