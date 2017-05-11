package wuxian.me.lagouspider.biz.tianyancha;

import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.JobManager;
import wuxian.me.spidersdk.SpiderCallback;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.log.LogManager;

import java.io.IOException;

/**
 * Created by wuxian on 6/5/2017.
 */
public class TianyanSpiderCallback extends SpiderCallback {
    public TianyanSpiderCallback(@NotNull BaseSpider spider) {
        super(spider);
    }

    public void onFailure(Call call, IOException e) {
        LogManager.error("onFailure: " + getSpider().name());
        JobManager.getInstance().fail(getSpider(), Fail.MAYBE_BLOCK);
        getSpider().serializeFullLog();
    }
}
