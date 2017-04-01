package wuxian.me.lagouspider.strategy;

import com.sun.istack.internal.NotNull;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * 爬虫抓取策略 防止ip被封
 */
public interface IStrategy extends Runnable {
    void setRunnable(@NotNull Runnable runnable);
}
