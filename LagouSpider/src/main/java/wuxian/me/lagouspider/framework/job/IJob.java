package wuxian.me.lagouspider.framework.job;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.framework.control.Fail;

import java.util.List;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * 爬虫抓取策略 防止ip被封
 */
public interface IJob extends Runnable {
    int STATE_INIT = 0;
    int STATE_SUCCESS = 2;
    int STATE_FAIL = 3;
    int STATE_RETRY = 4;

    int getCurrentState();

    void setCurrentState(int state);

    void fail(Fail fail);  //用于爬虫策略

    int getFailTimes();

    List<Fail> getFailures();

    void setRealRunnable(@NotNull Runnable runnable);

    Runnable getRealRunnable();
}