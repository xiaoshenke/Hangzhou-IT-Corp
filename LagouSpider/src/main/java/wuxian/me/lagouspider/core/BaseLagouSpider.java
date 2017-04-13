package wuxian.me.lagouspider.core;

import com.sun.istack.internal.NotNull;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.framework.control.Fail;
import wuxian.me.lagouspider.framework.control.JobMonitor;
import wuxian.me.lagouspider.framework.BaseSpider;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 9/4/2017.
 */
public abstract class BaseLagouSpider extends BaseSpider {
    public String name() {
        return simpleName();
    }

    public final String simpleName() {
        return getClass().getSimpleName();
    }

    @Override
    public final boolean checkBlockAndFailThisSpider(int httpCode) {
        if (httpCode == -1) {
            logger().error("We got BLOCKED, " + name());
            JobMonitor.getInstance().fail(BaseLagouSpider.this, Fail.BLOCK);
            return true;
        }
        return false;
    }

    @Override
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

    //Todo:打详细log到文件 包括request,response,html,spider的类型,时间等等等等...
    //文件名也应该不一样
    @Override
    public final void serializeFullLog() {
        ;
    }

    //For Log
    protected final void printPreviousBrother(@NotNull Node node) {
        Node real = node.getPreviousSibling();
        while (real != null) {
            logger().info("type: " + real.getClass().getSimpleName());
            logger().info("getText: " + real.getText());
            logger().info("toString: " + real.toString());
            logger().info("toPlainTextString: " + real.toPlainTextString());

            real = real.getPreviousSibling();
        }
    }

    protected final void printNextBrother(@NotNull Node node) {
        Node real = node.getNextSibling();
        while (real != null) {
            logger().info("type: " + real.getClass().getSimpleName());
            logger().info("getText: " + real.getText());
            logger().info("toString: " + real.toString());
            logger().info("toPlainTextString: " + real.toPlainTextString());

            real = real.getNextSibling();
        }
    }
}
