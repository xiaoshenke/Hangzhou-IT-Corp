package wuxian.me.lagouspider.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wuxian on 9/4/2017.
 * http://www.xdaili.cn/freeproxy.html
 *
 */
public class IPProxyTool {

    private static List<Proxy> ipPortList;

    private static AtomicInteger current;

    //http://www.xicidaili.com/ http://www.kxdaili.com/ 貌似已阵亡 --> 拉勾会屏蔽这个网站的ip
    //http://www.ip181.com/  --> 稳定性太差了 可能是国内用它的人太多？
    static {
        ipPortList = new ArrayList<Proxy>();

        ipPortList.add(new Proxy("200.196.233.58", 8080));
        ipPortList.add(new Proxy("111.13.7.121", 80));
        ipPortList.add(new Proxy("157.0.25.178", 808));

        current = new AtomicInteger(-1);
    }

    public static Proxy switchNextProxy() {
        if (current.get() + 1 == ipPortList.size()) {
            return null;
        }
        current.set((current.get() + 1) % ipPortList.size());

        Proxy proxy = ipPortList.get(current.get());
        System.setProperty("http.proxySet", "true");
        System.getProperties().setProperty("http.proxyHost", proxy.ip);
        System.getProperties().setProperty("http.proxyPort", String.valueOf(proxy.port));

        System.getProperties().setProperty("https.proxyHost", proxy.ip);
        System.getProperties().setProperty("https.proxyPort", String.valueOf(proxy.port));

        return proxy;
    }

    private IPProxyTool() {
    }

    public static class Proxy {
        public String ip;
        public int port;

        public Proxy(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }
    }
}
