package wuxian.me.spidersdk;

import com.sun.istack.internal.NotNull;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.anti.IPProxyTool;
import wuxian.me.spidersdk.job.IJob;

/**
 * Created by wuxian on 18/5/2017.
 * 统筹管理所有job,
 * 1 管理WorkThread,JobQueue,JobMonitor
 * 2 负责处理job的成功失败 --> 失败是否重试
 * 3 负责处理ip被屏蔽 --> 是则停止现有job,切换ip,打监控日志,重启workThread等等
 */
public interface IJobManager {

    boolean ipSwitched(final IPProxyTool.Proxy proxy);

    void success(Runnable runnable);

    void fail(@NotNull Runnable runnable, @NotNull Fail fail);

    void fail(@NotNull Runnable runnable, @NotNull Fail fail, boolean retry);

    IJob getJob();

    boolean putJob(@NotNull IJob job);

    void onDispatch(@NotNull BaseSpider spider);

    void start();

    boolean isEmpty();

}
