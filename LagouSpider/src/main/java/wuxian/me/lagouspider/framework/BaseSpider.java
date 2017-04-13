package wuxian.me.lagouspider.framework;

/**
 * Created by wuxian on 13/4/2017.
 */
public abstract class BaseSpider implements Runnable {

    public abstract boolean checkBlockAndFailThisSpider(int httpCode);

    protected abstract boolean checkBlockAndFailThisSpider(String html);
}
