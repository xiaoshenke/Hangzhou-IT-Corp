package wuxian.me.lagouspider.framework;

import com.sun.istack.internal.NotNull;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import wuxian.me.lagouspider.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by wuxian on 20/4/2017.
 * <p>
 * 每隔一段时间去ping一下http://www.ip138.com 确保proxy有效
 */
public class HeartbeatManager implements Runnable {

    long frequency = Config.PROXY_HEARTBEAT_FREQUENCY;

    private FutureTask<String> switchIPFuture;
    private Thread heartbeatThread = null;
    private int heartBeatTime = 0;
    private IPProxyTool.Proxy proxy;

    private HeartbeatManager() {
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

    private static HeartbeatManager instance;

    public static HeartbeatManager getInstance() {
        if (instance == null) {
            instance = new HeartbeatManager();
        }

        return instance;
    }

    public void setHeartbeatFrequency(long frequency) {
        this.frequency = frequency;
    }


    public void beginHeartBeat(IPProxyTool.Proxy proxy) {
        this.proxy = proxy;

        for (IHeartBeat heartBeat : heartBeatList) {
            heartBeat.onHeartBeatBegin();
        }

        heartbeatThread = new Thread(this);
        heartbeatThread.start();

    }

    //这个方法是给外部强制停止heartbeat用的 比如判定自己被屏蔽了 需要进行切换ip操作
    public void stopHeartBeat() {
        if (heartbeatThread != null) {
            heartbeatThread.interrupt();
        }
    }

    private List<IHeartBeat> heartBeatList = new ArrayList<IHeartBeat>();

    public void addHeartBeat(@NotNull IHeartBeat heartBeat) {
        heartBeatList.add(heartBeat);
    }

    //Todo
    public void run() {
        boolean proxyLive = true;
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(frequency);

            } catch (InterruptedException e) {
                break;
            }

            heartBeatTime++;
            for (IHeartBeat heartBeat : heartBeatList) {
                heartBeat.onHeartBeat(heartBeatTime);
            }

            new Thread(switchIPFuture).start();
            try {
                proxyLive = switchIPFuture.get() == null ? false : switchIPFuture.get().contains(proxy.ip);
            } catch (InterruptedException e1) {
                break;
            } catch (ExecutionException e) {
                //这个异常抛出的情况是你尝试去拿一个"被interrupt线程对应的"future.get
                //目前不用考虑这个
                break;
            }

            if (!proxyLive) {
                for (IHeartBeat heartBeat : heartBeatList) {
                    heartBeat.onHeartBeatFail();
                }
                return;
            }
        }
        for (IHeartBeat heartBeat : heartBeatList) {
            heartBeat.onHeartBeatInterrupt();
        }
    }


    public interface IHeartBeat {

        void onHeartBeatBegin();

        //这是第几个心跳包
        void onHeartBeat(int time);

        //没有对应的heartBeatSuccess,因为没有意义
        void onHeartBeatFail();

        void onHeartBeatInterrupt();
    }

}
