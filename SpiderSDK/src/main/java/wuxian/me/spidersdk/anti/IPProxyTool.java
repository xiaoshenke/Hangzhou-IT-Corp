package wuxian.me.spidersdk.anti;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import wuxian.me.spidersdk.util.FileUtil;
import wuxian.me.spidersdk.util.OkhttpProvider;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.log.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static wuxian.me.spidersdk.util.FileUtil.getCurrentPath;
import static wuxian.me.spidersdk.util.ShellUtil.*;


/**
 * Created by wuxian on 9/4/2017.
 * http://www.xdaili.cn/freeproxy.html   --> 还行
 * <p>
 * http://www.xicidaili.com/ http://www.kxdaili.com/ 貌似已阵亡 --> 拉勾会屏蔽这个网站的ip
 * http://www.ip181.com/  --> 稳定性太差了 可能是国内用它的人太多？
 */
public class IPProxyTool {

    CountDownLatch countDownLatch = new CountDownLatch(2);

    public static final String CUT = ";";
    public static final String SEPRATE = ":";

    private static List<Proxy> ipPortList;
    private Proxy currentProxy = null;

    static {
        ipPortList = new ArrayList<Proxy>();
    }

    private FutureTask<String> switchIPFuture;

    private boolean inited = false;

    public IPProxyTool() {
        init();
    }

    private void init() {
        if (inited) {
            return;
        }
        inited = true;

        FileUtil.writeToFile(getOpenProxyShellPath(), "open -t " + getProxyFilePath());

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

        if (JobManagerConfig.enableInitProxyFromFile) {
            ipPortList.clear();
            readProxyFromFile();
            FileUtil.writeToFile(getProxyFilePath(), "");  //清空文件
        }
    }

    String getProxyFilePath() {
        return getCurrentPath() + JobManagerConfig.ipproxyFile;
    }

    private void readProxyFromFile() {
        String content = FileUtil.readFromFile(getProxyFilePath());
        if (content != null) {
            String[] proxys = content.split(CUT);
            if (proxys == null) {
                return;
            }
            for (int i = 0; i < proxys.length; i++) {
                String[] proxy = proxys[i].split(SEPRATE);
                if (proxy != null && proxy.length == 2) {
                    //Todo:正则检查ip和端口
                    ipPortList.add(new Proxy(proxy[0], Integer.parseInt(proxy[1])));
                }
            }
        }
    }

    public Proxy switchNextProxy() {
        if (ipPortList.size() == 0) {
            return null;
        }
        Proxy proxy = ipPortList.get(0);  //get and remove
        currentProxy = proxy;
        ipPortList.remove(0);

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

    //支持运行时手工输入最新的proxy
    public void openShellAndEnsureProxyInputed() {
        if (countDownLatch.getCount() != 2) {  //init state
            return;
        }
        //logger().info("Begin openShellAndEnsureProxyInputed");
        countDownLatch.countDown();             //job begin

        new Thread() {
            @Override
            public void run() {
                while (textEditState() != 1) {
                    //没有调用open命令但是TextEdit进程正在运行 睡眠处理
                    try {
                        sleep(2000);
                        LogManager.warn("Try To Open TextEdit.exe For Writting IpProxy,but it's already running,wait...");
                    } catch (InterruptedException e) {
                        ;
                    }
                }

                openTextEdit();
                ipPortList.clear();

                boolean b = true;
                do {
                    try {
                        sleep(JobManagerConfig.shellCheckProxyFileSleepTime);    //每过10s检测文件是否有新的proxy ip写入,若没有,一直重试直到成功
                    } catch (InterruptedException e) {
                        ;
                    }
                    readProxyFromFile();
                    b = (ipPortList.size() == 0);
                    if (b) {
                        if (textEditState() == 1) { //重新打开文件
                            openTextEdit();
                        }
                    }
                } while (b);

                FileUtil.writeToFile(getProxyFilePath(), "");  //清空文件
                countDownLatch.countDown();
            }
        }.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            ;
        }

        //logger().info("Return from openShellAndEnsureProxyInputed");
        countDownLatch = new CountDownLatch(2);
    }

    public static class Proxy {
        public String ip;
        public int port;

        public Proxy(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Proxy proxy = (Proxy) o;

            if (port != proxy.port) return false;
            return ip != null ? ip.equals(proxy.ip) : proxy.ip == null;

        }

        //标准hashcode的实现
        @Override
        public int hashCode() {
            int result = ip != null ? ip.hashCode() : 0;
            result = 31 * result + port;
            return result;
        }
    }
}
