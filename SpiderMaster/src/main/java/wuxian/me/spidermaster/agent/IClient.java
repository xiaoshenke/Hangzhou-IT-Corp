package wuxian.me.spidermaster.agent;

import wuxian.me.spidermaster.rpc.IRpcCallback;
import wuxian.me.spidermaster.rpc.RpcRequest;

/**
 * Created by wuxian on 26/5/2017.
 */
public interface IClient {

    void asyncConnect(String serverIp, int serverPort);

    boolean isConnected();

    //主动调用disconnect
    void disconnectFromServer();

    void onMessage(RpcRequest request);

    void asyncSendMessage(RpcRequest request, IRpcCallback callback);

    void onDisconnectByServer();

}
