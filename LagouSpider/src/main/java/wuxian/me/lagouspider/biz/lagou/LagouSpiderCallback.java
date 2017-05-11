package wuxian.me.lagouspider.biz.lagou;

import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.JobManager;
import wuxian.me.spidersdk.SpiderCallback;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.log.LogManager;


import java.io.IOException;

/**
 * Created by wuxian on 16/4/2017.
 */
public class LagouSpiderCallback extends SpiderCallback {

    public LagouSpiderCallback(@NotNull BaseSpider spider) {
        super(spider);
    }

    //拉勾返回onFailure可能就是被屏蔽了
    public final void onFailure(Call call, IOException e) {
        LogManager.error("onFailure: " + getSpider().name());
        JobManager.getInstance().fail(getSpider(), Fail.MAYBE_BLOCK);
        getSpider().serializeFullLog();
    }
}
