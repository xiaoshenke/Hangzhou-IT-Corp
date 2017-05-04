package wuxian.me.spidersdk.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.spidersdk.job.IJob;

/**
 * Created by wuxian on 4/5/2017.
 * <p>
 * job queue接口
 * <p>
 * Fixme:尝试使用redis来实现jobqueue 遇到BaseSpider的序列化问题...
 */
public interface IQueue {

    boolean putJob(@NotNull IJob job);

    IJob getJob();

    boolean isEmpty();

    int getJobNum();
}
