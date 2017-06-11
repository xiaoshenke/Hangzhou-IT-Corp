package wuxian.me.spidermaster.master.biz;

import io.netty.channel.socket.SocketChannel;
import wuxian.me.spidermaster.master.RpcBizHandler;
import wuxian.me.spidermaster.rpc.RpcRequest;

/**
 * Created by wuxian on 11/6/2017.
 * <p>
 * Todo:
 */

@RpcBizHandler(methodName = "reportStatus")
public class ReportStatusHandler implements IBizHandler {

    public void handleRequest(RpcRequest request, SocketChannel channel) {

    }
}
