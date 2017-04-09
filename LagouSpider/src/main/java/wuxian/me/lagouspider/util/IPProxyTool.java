package wuxian.me.lagouspider.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wuxian on 9/4/2017.
 */
public class IPProxyTool {

    private static List<Proxy> ipPortList;

    private static AtomicInteger current;

    static {
        ipPortList = new ArrayList<Proxy>();

        ipPortList.add(new Proxy("1.83.120.48", 80));
        ipPortList.add(new Proxy("35.185.44.214", 80));
        ipPortList.add(new Proxy("110.170.201.227", 8080));
        ipPortList.add(new Proxy("202.143.189.130", 8080));
        ipPortList.add(new Proxy("202.106.16.36", 3128));
        ipPortList.add(new Proxy("171.13.37.103", 808));
        ipPortList.add(new Proxy("121.204.165.246", 8118));
        ipPortList.add(new Proxy("36.249.25.72", 808));
        ipPortList.add(new Proxy("115.230.11.193", 808));

        current = new AtomicInteger(ipPortList.size() - 1);
    }

    public static void switchNextProxy() {
        current.set((current.get() + 1) % ipPortList.size());

        Proxy proxy = ipPortList.get(current.get());
        System.setProperty("http.proxySet", "true");
        System.getProperties().setProperty("http.proxyHost", proxy.ip);
        System.getProperties().setProperty("http.proxyPort", String.valueOf(proxy.port));

        System.getProperties().setProperty("https.proxyHost", proxy.ip);
        System.getProperties().setProperty("https.proxyPort", String.valueOf(proxy.port));
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
