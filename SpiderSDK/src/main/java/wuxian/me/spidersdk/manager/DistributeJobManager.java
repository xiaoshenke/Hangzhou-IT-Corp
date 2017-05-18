package wuxian.me.spidersdk.manager;

import com.sun.istack.internal.NotNull;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.IJobManager;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.anti.IPProxyTool;
import wuxian.me.spidersdk.control.IQueue;
import wuxian.me.spidersdk.control.RedisJobQueue;
import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.job.IJob;

/**
 * Created by wuxian on 18/5/2017.
 *
 * Todo:分布式下(且没有身份的)的job manager
 */
public class DistributeJobManager implements IJobManager{

    private IQueue queue;
    private ClassHelper.CheckFilter checkFilter;

    public void setCheckFilter(@NotNull ClassHelper.CheckFilter filter){
        this.checkFilter = filter;
    }

    public DistributeJobManager(){
        queue = new RedisJobQueue(checkFilter);
    }

    public boolean ipSwitched(IPProxyTool.Proxy proxy) {
        return false;
    }

    public void success(Runnable runnable) {

    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail) {

    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail, boolean retry) {

    }

    public IJob getJob() {
        return null;
    }

    public boolean putJob(@NotNull IJob job) {
        return false;
    }

    public void register(@NotNull BaseSpider spider) {

    }

    public void start() {

    }
}
