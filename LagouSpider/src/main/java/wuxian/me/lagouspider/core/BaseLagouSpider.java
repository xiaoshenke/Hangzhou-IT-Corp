package wuxian.me.lagouspider.core;

import com.sun.istack.internal.NotNull;
import okhttp3.Response;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.lagouspider.framework.FileUtil;
import wuxian.me.lagouspider.framework.SpiderCallback;
import wuxian.me.lagouspider.framework.control.Fail;
import wuxian.me.lagouspider.framework.control.JobMonitor;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.util.Helper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 9/4/2017.
 */
public abstract class BaseLagouSpider extends BaseSpider {

    protected abstract String getRequestString();

    private SpiderCallback callback;

    protected SpiderCallback getCallback() {
        return callback;
    }

    public BaseLagouSpider() {
        callback = new SpiderCallback(this);
    }

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

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat fullLogSdf = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");

    @Override
    public final void serializeFullLog() {
        StringBuilder builder = new StringBuilder("");
        Date date = new Date();
        String time = sdf.format(date);
        builder.append(time);

        builder.append(" [" + Thread.currentThread().getName() + "]/n");
        builder.append("Spider: " + toString() + "/n");

        builder.append("Request: " + getRequestString() + "/n");
        Response response = getCallback().getResponse();
        if (response != null) {
            builder.append("Response: HttpCode: " + getCallback().getResponse().code() + " isRedirect: " + getCallback().getResponse().isRedirect() + " Message: " + getCallback().getResponse().message() + "/n");
            builder.append("Header: " + getCallback().getResponse().headers().toString() + "/n");

            try {
                builder.append("/nBody: " + response.body().string());
            } catch (IOException e) {
                if (response.body() != null) {
                    response.body().close();
                }
            }
        }
        String fileName = fullLogSdf.format(date) + simpleName(); //simpleName只有一个类名
        FileUtil.writeToFile(Helper.getFullLogFilePath(fileName), builder.toString());

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
}
