package wuxian.me.spidermaster;

import wuxian.me.spidermaster.rpc.IRpcCallback;
import wuxian.me.spidermaster.rpc.RpcRequest;

/**
 * Created by wuxian on 26/5/2017.
 * <p>
 * Todo:
 */
public class SpiderClient implements IClient {

    public void asyncConnect(String serverIp, int serverPort) {

    }

    public boolean isConnected() {
        return false;
    }

    public void disconnectFromServer() {

    }

    //子线程收到RpcRequest 于是主线程"看见"了这个RpcRequest(应该说是指令 比如暂停pause指令),并做出相应的应对
    //目前的设计 这个响应的实现是子线程自动返回一个成功码
    //因为不应该由主线程 也就是业务线程去发这个成功码
    //这样的设计不知道有没有坑...
    public void onMessage(RpcRequest request) {

    }

    //内部一定是将这个任务(RpcRequest)丢到子线程去发这个请求
    //注意这里的callback在子线程执行
    public void asyncSendMessage(RpcRequest request, IRpcCallback callback) {

    }

    public void onDisconnectByServer() {

    }
}
