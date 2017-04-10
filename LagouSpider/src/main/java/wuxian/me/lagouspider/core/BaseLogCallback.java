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
        logger().error("onFailure:" + " spider: " + spider.name());
    }

    public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
            if (spider.checkBlockAndFailThisSpider(response.code())) {
                ;
            } else {
                logger().error("HttpCode: " + response.code() + " message: " + response.message() + " spider: " + spider.name());
            }
        }

    }
}
