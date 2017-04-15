package wuxian.me.lagouspider.util;

import com.sun.istack.internal.NotNull;
import org.htmlparser.Node;
import org.htmlparser.util.NodeList;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 15/4/2017.
 */
public class NodeLogUtil {

    private NodeLogUtil() {
    }

    public static final void printNodeOnly(@NotNull Node node) {
        logger().info("type: " + node.getClass().getSimpleName());
        logger().info("getText: " + node.getText());
        logger().info("toString: " + node.toString());
        logger().info("toPlainTextString: " + node.toPlainTextString());
    }

    public static final void printChildrenOfNode(@NotNull Node node) {
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
    public static final void printPreviousBrother(@NotNull Node node) {
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
    public static final void printNextBrother(@NotNull Node node) {
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
