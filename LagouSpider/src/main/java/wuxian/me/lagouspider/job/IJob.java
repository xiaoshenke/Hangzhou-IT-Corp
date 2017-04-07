package wuxian.me.lagouspider.job;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.control.Fail;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * 爬虫抓取策略 防止ip被封
 *
 * Fixme:每个job都独自的job ID?
 */
public interface IJob extends Runnable {
    int STATE_INIT = 0;
    int STATE_IN_PROGRESS = 1;
    int STATE_SUCCESS = 2;
    int STATE_FAIL = 3;
    int STATE_RETRY = 4;

    int getCurrentState();

    void setCurrentState(int state);

    void fail(Fail fail);  //用于爬虫策略

    int getFailTimes();

    void setRealRunnable(@NotNull Runnable runnable);

    Runnable getRealRunnable();
}
