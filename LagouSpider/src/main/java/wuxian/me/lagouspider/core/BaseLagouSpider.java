package wuxian.me.lagouspider.core;

import com.sun.istack.internal.NotNull;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.control.Fail;
import wuxian.me.lagouspider.control.JobMonitor;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 9/4/2017.
 */
public abstract class BaseLagouSpider implements Runnable {
    public String name() {
        return simpleName();
    }

    public final String simpleName() {
        return getClass().getSimpleName();
    }

    public final boolean checkBlockAndFailThisSpider(int httpCode) {
        if (httpCode == -1) {
            logger().error("We got BLOCKED, " + name());
            JobMonitor.getInstance().fail(BaseLagouSpider.this, Fail.BLOCK);
            return true;
        }
        return false;
    }

    protected boolean checkBlockAndFailThisSpider(String html) {
        try {
            Parser parser = new Parser(html);
            parser.setEncoding("utf-8");
            HasAttributeFilter filter = new HasAttributeFilter("class", "i_error");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            if (list != null && list.size() != 0) {
                logger().error("We got BLOCKED, " + name());
                JobMonitor.getInstance().fail(BaseLagouSpider.this, Fail.BLOCK);
                return true;
            }

            return false;
        } catch (ParserException e) {
            return false;
        }
    }
}
