package wuxian.me.lagouspider.biz.lagou;

import com.sun.istack.internal.Nullable;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.SpiderCallback;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.manager.JobManagerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuxian on 9/4/2017.
 * 这个类继承自framework包中的BaseSpider,主要是不同网站的BaseSpider认定被屏蔽的现象是不一致的。
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
                JobManagerFactory.getJobManager().fail(BaseLagouSpider.this, Fail.BLOCK);
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

    @Nullable
    protected Node getFirstMatch(@Nullable NodeList list) {

        if (list != null && list.size() != 0) {
            return list.elementAt(0);
        }

        return null;
    }

    @Nullable
    protected Node getFirstMatchIfNullThrow(@Nullable NodeList list) throws ParserException {
        Node node = getFirstMatch(list);

        if (node == null) {
            throw new ParserException();
        }

        return node;
    }

    protected boolean stringEmptyIfTrueThrow(String s) throws ParserException {
        boolean b = s == null || s.length() == 0;

        if (b) {
            throw new ParserException();
        }

        return b;
    }

    protected boolean listEmpty(NodeList list) {
        return list == null || list.size() == 0;
    }

    protected boolean listEmptyIfTrueThrow(NodeList list) throws ParserException {
        boolean b = listEmpty(list);

        if (b) {
            throw new ParserException();
        }

        return b;
    }

    @Nullable
    public static String getMatchedGroup(Pattern pattern, String origin) {
        if (origin == null || origin.length() == 0 || pattern == null) {
            return null;
        }

        Matcher matcher = pattern.matcher(origin);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    @Nullable
    public static String getMatchedGroupHungry(Pattern pattern, String origin) {
        if (origin == null || origin.length() == 0 || pattern == null) {
            return null;
        }

        Matcher matcher = pattern.matcher(origin);

        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }

    public static Integer getMatchedGroupInteger(Pattern pattern, String origin) {
        String match = getMatchedGroup(pattern, origin);

        return match == null ? null : Integer.parseInt(match);
    }

    public static Long getMatchedGroupLong(Pattern pattern, String origin) {
        String match = getMatchedGroup(pattern, origin);

        return match == null ? null : Long.parseLong(match);
    }
}
