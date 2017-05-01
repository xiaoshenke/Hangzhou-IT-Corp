package wuxian.me.lagouspider.framework;

import okhttp3.OkHttpClient;
import wuxian.me.lagouspider.framework.control.JobManager;
import wuxian.me.lagouspider.framework.control.JobManagerConfig;

import java.util.concurrent.TimeUnit;

/**
 * Created by wuxian on 1/4/2017.
 */
public class OkhttpProvider {

    private static OkHttpClient client;

    private OkhttpProvider() {
    }

    public static OkHttpClient getClient() {

        if (client == null) {
            client = new OkHttpClient.Builder().readTimeout(JobManagerConfig.okhttpClientSocketReadTimeout,
                    TimeUnit.MILLISECONDS).build();
        }
        return client;
    }
}
