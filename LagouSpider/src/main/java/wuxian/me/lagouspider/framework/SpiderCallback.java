package wuxian.me.lagouspider.framework;

import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import wuxian.me.lagouspider.framework.control.Fail;
import wuxian.me.lagouspider.framework.control.FailureManager;
import wuxian.me.lagouspider.framework.control.JobMonitor;

import java.io.IOException;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 10/4/2017.
 * <p>
 */
public final class SpiderCallback implements Callback {
    private BaseSpider spider;

    private Response response;

    public Response getResponse() {
        return response;
    }

    //先由protect改成private
    private final BaseSpider getSpider() {
        return spider;
    }

    public SpiderCallback(@NotNull BaseSpider spider) {
        this.spider = spider;
        FailureManager.register(spider);
    }

    //OkHttpClient的Callback不应该是跑在Main里面的么？
    public final void onFailure(Call call, IOException e) {
        logger().error("onFailure: spider: " + spider.name());
        JobMonitor.getInstance().fail(spider, Fail.MAYBE_BLOCK);
        spider.serializeFullLog();
    }

    public final void onResponse(Call call, Response response) throws IOException {
        this.response = response;
        if (!response.isSuccessful()) {
            logger().error("HttpCode: " + response.code() + " spider: " + spider.name()); //console尽量少log

            if (spider.checkBlockAndFailThisSpider(response.code())) {
                logger().error("We got BLOCKED, " + spider.name());
                JobMonitor.getInstance().fail(spider, Fail.BLOCK);
            } else {
                JobMonitor.getInstance().fail(spider, new Fail(response.code(), response.message()));
            }
            if (response.body() != null) {
                response.body().close();
            }
            spider.serializeFullLog();

        } else {
            String body = response.body().string();

            int result = spider.parseRealData(body);
            if (result == BaseSpider.RET_SUCCESS) {
                JobMonitor.getInstance().success(spider);

            } else if (result == BaseSpider.RET_PARSING_ERR) {

                JobMonitor.getInstance().fail(spider, Fail.MAYBE_BLOCK, false);  //这个不放入重试队列
                spider.serializeFullLog();

            } else if (result == BaseSpider.RET_MAYBE_BLOCK) {

                if (spider.checkBlockAndFailThisSpider(body)) {
                    logger().error("We got BLOCKED, " + spider.name());
                    JobMonitor.getInstance().fail(spider, Fail.BLOCK);
                    spider.serializeFullLog();
                } else {
                    JobMonitor.getInstance().success(spider);
                }
            }
        }
    }
}
