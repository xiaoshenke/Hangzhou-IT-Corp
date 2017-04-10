package wuxian.me.lagouspider.core;

import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 10/4/2017.
 * <p>
 * Used for LOG.
 */
public class BaseLogCallback implements Callback {

    private BaseLagouSpider spider;

    public BaseLogCallback(@NotNull BaseLagouSpider spider) {
        this.spider = spider;
    }

    public void onFailure(Call call, IOException e) {
        logger().error("HttpCode: -1" + " spider: " + spider.simpleName());
    }

    public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
            logger().error("HttpCode: " + response.code() + " spider: " + spider.name());
        }

    }
}
