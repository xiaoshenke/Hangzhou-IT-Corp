package wuxian.me.lagouspider.core;

import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.framework.SpiderCallback;
import wuxian.me.lagouspider.framework.control.Fail;
import wuxian.me.lagouspider.framework.control.JobResultManager;

import java.io.IOException;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 16/4/2017.
 */
public class LagouSpiderCallback extends SpiderCallback {

    public LagouSpiderCallback(@NotNull BaseSpider spider) {
        super(spider);
    }

    //拉勾返回onFailure可能就是被屏蔽了
    public final void onFailure(Call call, IOException e) {
        logger().error("onFailure: " + getSpider().name());
        JobResultManager.getInstance().fail(getSpider(), Fail.MAYBE_BLOCK);
        getSpider().serializeFullLog();
    }
}
