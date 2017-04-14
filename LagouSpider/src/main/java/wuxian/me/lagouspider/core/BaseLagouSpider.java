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
 *
 * 这个类继承自framework包中的BaseSpider,主要是不同网站的BaseSpider认定被屏蔽的现象是不一致的。
 *
 * 子类需要实现的方法
 * 1 Request buildRequest()
 * 2 parseRealData(String data)
 * 3 name(),fullName();
 */
public abstract class BaseLagouSpider extends BaseSpider {

    @Override
    public final boolean checkBlockAndFailThisSpider(int httpCode) {
        if (httpCode == -1 || httpCode == 404) {
            return true;
        }
        return false;
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
                logger().error("We got BLOCKED, " + name());
                JobMonitor.getInstance().fail(BaseLagouSpider.this, Fail.BLOCK);
                return true;
            }

            return false;
        } catch (ParserException e) {
            return false;
        }
    }

    protected final void printNodeOnly(@NotNull Node node) {
        logger().info("type: " + node.getClass().getSimpleName());
        logger().info("getText: " + node.getText());
        logger().info("toString: " + node.toString());
        logger().info("toPlainTextString: " + node.toPlainTextString());
    }

    protected final void printChildrenOfNode(@NotNull Node node) {
        NodeList children = node.getChildren();
        if (children == null || children.size() == 0) {
            return;
        }

        for (int i = 0; i < children.size(); i++) {
            Node child = children.elementAt(i);
            printNodeOnly(child);
        }
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

    //For Log
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

    @Override
    public String name() {
        return simpleName();
    }

    @Override
    public String fullName() {
        return name();
    }
}
