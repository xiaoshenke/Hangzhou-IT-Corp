package wuxian.me.lagouspider.framework;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.util.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

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
        ipPortList.add(new Proxy("114.237.6.178", 40430));

        FileUtil.writeToFile(Helper.getOpenProxyShellPath(), "open -t " + Helper.getProxyFilePath());
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

        if (Config.ProxyControl.ENABLE_READ_PROXY_FROM_FILE) {
            ipPortList.clear();
            readProxyFromFile();
            FileUtil.writeToFile(Helper.getProxyFilePath(), "");  //清空文件
        }
    }

    private void readProxyFromFile() {
        String content = FileUtil.readFromFile(Helper.getProxyFilePath());
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

    //0:running 1:not running -1:not known
    private int textEditState() {
        try {
            return isTextEditRunning() ? 0 : 1;
        } catch (IOException e) {
            return -1;
        }
    }

    public static boolean isTextEditRunning() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String check = Helper.getCheckProcessShellPath();
        String[] args = new String[]{check, "TextEdit"};
        Process pc = null;
        pc = runtime.exec(args);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(pc.getInputStream()));

        StringBuilder builder = new StringBuilder("");
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString().contains("/TextEdit");  //contains方法非正则 不用特殊处理
    }

    private boolean openTextEdit() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(FileUtil.readFromFile(Helper.getOpenProxyShellPath()));
            int exit = proc.waitFor();
            return true;
        } catch (Exception e) {
            return false;
        }
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
                        logger().warn("Try To Open TextEdit.exe For Writting IpProxy,but it's already running,wait...");
                    } catch (InterruptedException e) {
                        ;
                    }
                }

                openTextEdit();
                ipPortList.clear();

                boolean b = true;
                do {
                    try {
                        sleep(Config.Shell.SLEEP_TIME_CHECK_PROXY_INPUTED);    //每过10s检测文件是否有新的proxy ip写入,若没有,一直重试直到成功
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

                FileUtil.writeToFile(Helper.getProxyFilePath(), "");  //清空文件
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

        @Override
        public int hashCode() {
            int result = ip != null ? ip.hashCode() : 0;
            result = 31 * result + port;
            return result;
        }
    }
}
