package wuxian.me.lagouspider.core;

import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.control.Fail;
import wuxian.me.lagouspider.control.JobMonitor;

/**
 * Created by wuxian on 9/4/2017.
 */
public abstract class BaseLagouSpider implements Runnable {

    abstract public String simpleName();

    protected boolean checkBlockAndFailThisSpider(String html) {
        try {
            Parser parser = new Parser(html);
            parser.setEncoding("utf-8");
            HasAttributeFilter filter = new HasAttributeFilter("class", "i_error");

            NodeList list = parser.extractAllNodesThatMatch(filter);
            if (list != null) {
                JobMonitor.getInstance().fail(BaseLagouSpider.this, Fail.BLOCK);
                return true;
            }

            return false;
        } catch (ParserException e) {
            return false;
        }

    }
}
