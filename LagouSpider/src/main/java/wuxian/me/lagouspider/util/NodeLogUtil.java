package wuxian.me.lagouspider.util;

import com.sun.istack.internal.NotNull;
import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import wuxian.me.spidersdk.log.LogManager;

/**
 * Created by wuxian on 15/4/2017.
 */
public class NodeLogUtil {

    private NodeLogUtil() {
    }

    public static final void printNodeOnly(@NotNull Node node) {
        LogManager.info("type: " + node.getClass().getSimpleName());
        LogManager.info("getText: " + node.getText());
        LogManager.info("toString: " + node.toString());
        LogManager.info("toPlainTextString: " + node.toPlainTextString());
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
            LogManager.info("type: " + real.getClass().getSimpleName());
            LogManager.info("getText: " + real.getText());
            LogManager.info("toString: " + real.toString());
            LogManager.info("toPlainTextString: " + real.toPlainTextString());

            real = real.getPreviousSibling();
        }
    }

    //For Log
    public static final void printNextBrother(@NotNull Node node) {
        Node real = node.getNextSibling();
        while (real != null) {
            LogManager.info("type: " + real.getClass().getSimpleName());
            LogManager.info("getText: " + real.getText());
            LogManager.info("toString: " + real.toString());
            LogManager.info("toPlainTextString: " + real.toPlainTextString());

            real = real.getNextSibling();
        }
    }
}
