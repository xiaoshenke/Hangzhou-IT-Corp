package wuxian.me.spidermaster.agent;

import io.netty.channel.socket.SocketChannel;
import wuxian.me.spidermaster.rpc.IRpcCallback;
import wuxian.me.spidermaster.rpc.RpcRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuxian on 26/5/2017.
 * <p>
 */
public class SpiderClient implements IClient {

    private Thread connectThread;
    private boolean connected = false;
    private SocketChannel channel;

    public void asyncConnect(final String serverIp, final int serverPort) {
        if (connected) {
            return;
        }

        SpiderConnector connector = new SpiderConnector(
                serverIp, serverPort, this, new SpiderConnector.IConnectCallback() {
            public void onSuccess(SocketChannel channel) {
                SpiderClient.this.channel = channel;  //save channel
                connected = true;
            }

            public void onFail() {

            }

            public void onException() {

            }
        });

        connectThread = new Thread(connector);
        connectThread.setName("ConnectionThread");
        connectThread.start();

    }

    public boolean isConnected() {
        return connected;
    }

    public void disconnectFromServer() {

    }

    //子线程收到RpcRequest 于是主线程"看见"了这个RpcRequest(应该说是指令 比如暂停pause指令),并做出相应的应对
    //目前的设计 这个响应的实现是子线程自动返回一个成功码
    //因为不应该由主线程 也就是业务线程去发这个成功码
    //这样的设计不知道有没有坑...
    public void onMessage(RpcRequest request) {

        if (request == null) {
            return;
        }

        if (!requestMap.containsKey(request)) {
            return;
        }

        requestMap.get(request).onRpcRequest(request);
    }

    //内部一定是将这个任务(RpcRequest)丢到子线程去发这个请求
    //注意这里的callback在子线程执行
    //Todo:实现request线程
    public void asyncSendMessage(RpcRequest request, IRpcCallback callback) {

    }

    public void onDisconnectByServer() {
        connected = false;
    }

    private static Map<RpcRequest, OnRpcRequest> requestMap = new HashMap<RpcRequest, OnRpcRequest>();


    //业务层只需实现OnRpcRequest接口并注册即可
    public static void registerMessageNotify(RpcRequest request, OnRpcRequest onRpcRequest) {
        if (request == null || onRpcRequest == null) {
            return;
        }

        if (requestMap.containsKey(request)) {
            return;
        }

        requestMap.put(request, onRpcRequest);
    }

    //处理spider master的命令
    public interface OnRpcRequest {
        void onRpcRequest(RpcRequest request);
    }
}
