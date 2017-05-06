package wuxian.me.lagouspider.business.lagou;

import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.JobManager;
import wuxian.me.spidersdk.SpiderCallback;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.log.LogManager;

/**
 * Created by wuxian on 9/4/2017.
 *
 * 这个类继承自framework包中的BaseSpider,主要是不同网站的BaseSpider认定被屏蔽的现象是不一致的。
 *
 * 子类需要实现的方法
 * 1 Request buildRequest()
 * 2 parseRealData(String data)
 * 3 product_name(),hashString();
 * 4 override toString()方法来规范命名
 */
public abstract class BaseLagouSpider extends BaseSpider {

    @Override
    protected final SpiderCallback getCallback() {
        return new LagouSpiderCallback(this);
    }

    //子类可以自己实现
    @Override
    protected boolean checkBlockAndFailThisSpider(String html) {
        try {
            Parser parser = new Parser(html);
            parser.setEncoding("utf-8");
            HasAttributeFilter filter = new HasAttributeFilter("class", "i_error");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            if (list != null && list.size() != 0) {
                LogManager.error("We got BLOCKED, " + name());
                JobManager.getInstance().fail(BaseLagouSpider.this, Fail.BLOCK);
                return true;
            }

            return false;
        } catch (ParserException e) {
            return false;
        }
    }


    @Override
    public String hashString() {
        return name();
    }
}
