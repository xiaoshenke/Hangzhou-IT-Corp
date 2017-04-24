package wuxian.me.lagouspider.framework;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wuxian on 9/4/2017.
 * http://www.xdaili.cn/freeproxy.html   --> 还行
 * <p>
 * http://www.xicidaili.com/ http://www.kxdaili.com/ 貌似已阵亡 --> 拉勾会屏蔽这个网站的ip
 * http://www.ip181.com/  --> 稳定性太差了 可能是国内用它的人太多？
 */
public class IPProxyTool {

    private static List<Proxy> ipPortList;

    static {
        ipPortList = new ArrayList<Proxy>();
        ipPortList.add(new Proxy("180.102.204.225", 49735));
    }

    private AtomicInteger current;

    private FutureTask<String> switchIPFuture;

    public IPProxyTool() {
        init();
    }

    private void init() {
        current = new AtomicInteger(-1);

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://www.ip138.com/ip2city.asp").newBuilder();
        Headers.Builder builder = new Headers.Builder();
        builder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        final Request request = new Request.Builder()
                .headers(builder.build())
                .url(urlBuilder.build().toString())
                .build();

        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                try {
                    Response response = OkhttpProvider.getClient().newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    return null;
                }
            }
        };
        switchIPFuture = new FutureTask<String>(callable);
    }

    public Proxy switchNextProxy() {
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

    public boolean ensureIpSwitched(final IPProxyTool.Proxy proxy)
            throws InterruptedException, ExecutionException {
        new Thread(switchIPFuture).start();
        if (switchIPFuture.get() == null) {
            return false;
        }

        boolean b = switchIPFuture.get().contains(proxy.ip);
        return b;
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
