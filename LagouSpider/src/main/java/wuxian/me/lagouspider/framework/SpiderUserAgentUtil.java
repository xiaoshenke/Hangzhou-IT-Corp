package wuxian.me.lagouspider.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wuxian on 24/4/2017.
 */
public class SpiderUserAgentUtil {

    private static List<String> agentList;

    static {
        agentList = new ArrayList<String>();

        agentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

        agentList.add("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; InfoPath.3; rv:11.0) like Gecko");

        agentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");

        agentList.add("Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11");

        agentList.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)");

        agentList.add("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");

        agentList.add("Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");

        agentList.add("Mozilla/5.0 (SymbianOS/9.4; Series60/5.0 NokiaN97-1/20.0.019; Profile/MIDP-2.1 Configuration/CLDC-1.1) AppleWebKit/525 (KHTML, like Gecko) BrowserNG/7.1.18124");

        agentList.add("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

        agentList.add("Googlebot/2.1 (+http://www.googlebot.com/bot.html)");

        agentList.add("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");

        agentList.add("Googlebot/2.1 (+http://www.google.com/bot.html)");

        agentList.add("Mozilla/5.0 (compatible; Yahoo! Slurp China; http://misc.yahoo.com.cn/help.html)");

        agentList.add("Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)");

        agentList.add("Mozilla/5.0 (compatible; iaskspider/1.0; MSIE 6.0)");

        agentList.add("Sogou web spider/3.0(+http://www.sogou.com/docs/help/webmasters.htm#07)");

        agentList.add("Sogou Push Spider/3.0(+http://www.sogou.com/docs/help/webmasters.htm#07)");

        agentList.add("Mozilla/5.0 (compatible; YodaoBot/1.0;http://www.yodao.com/help/webmaster/spider/;)");

        agentList.add("msnbot/1.0 (+http://search.msn.com/msnbot.htm)");
    }

    private SpiderUserAgentUtil() {
    }

    private static Random random = new Random();

    public static String next() {
        return next(true);
    }

    private static int index = -1;

    public static String next(boolean isRandom) {
        if (isRandom) {
            int pos = (int) (agentList.size() * random.nextDouble());
            return agentList.get(pos);
        } else {
            ++index;
            if (index >= agentList.size()) {
                return null;
            }
            return agentList.get(index);
        }
    }
}
