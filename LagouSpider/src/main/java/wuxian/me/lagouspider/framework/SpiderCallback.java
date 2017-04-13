package wuxian.me.lagouspider.framework;

import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import wuxian.me.lagouspider.framework.control.Fail;
import wuxian.me.lagouspider.framework.control.FailureManager;
import wuxian.me.lagouspider.framework.control.JobMonitor;
import wuxian.me.lagouspider.core.BaseLagouSpider;

import java.io.IOException;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 10/4/2017.
 * <p>
 */
public final class SpiderCallback implements Callback {
    private BaseSpider spider;

    protected final BaseSpider getSpider() {
        return spider;
    }

    public SpiderCallback(@NotNull BaseLagouSpider spider) {
        this.spider = spider;
        FailureManager.register(spider);
    }

    public final void onFailure(Call call, IOException e) {
        logger().error("onFailure:" + " spider: " + spider.name());
        JobMonitor.getInstance().fail(spider, Fail.NETWORK_ERR);
    }

    public final void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
            if (spider.checkBlockAndFailThisSpider(response.code())) {
                if (response.body() != null) {
                    response.body().close();
                }
                return;
            } else {
                logger().error("HttpCode: " + response.code() + " message: " + response.message() + " spider: " + spider.name());
            }
            JobMonitor.getInstance().fail(getSpider(), new Fail(response.code(), response.message()));
            if (response.body() != null) {
                response.body().close();
            }
            return;
        }

        if (spider.parseRealData(response.body().string())) {  //Fixme: 如果失败呢？
            JobMonitor.getInstance().success(getSpider());
        }
    }
}
