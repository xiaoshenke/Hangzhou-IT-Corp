package wuxian.me.lagouspider.job;

import com.sun.istack.internal.NotNull;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * 爬虫抓取策略 防止ip被封
 *
 * Fixme:每个job都独自的job ID?
 */
public interface IJob extends Runnable {
    void setRealRunnable(@NotNull Runnable runnable);
}
