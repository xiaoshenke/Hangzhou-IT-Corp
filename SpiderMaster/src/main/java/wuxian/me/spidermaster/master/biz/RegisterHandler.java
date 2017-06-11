package wuxian.me.spidermaster.master.biz;

import io.netty.channel.socket.SocketChannel;
import wuxian.me.spidermaster.master.core.RpcBizHandler;
import wuxian.me.spidermaster.rpc.RpcRequest;

/**
 * Created by wuxian on 18/5/2017.
 * <p>
 * Todo:
 */

@RpcBizHandler(methodName = "register")
public class RegisterHandler extends BaseBizHandler {

    public void handleRequest(RpcRequest request, SocketChannel channel) {

    }
}
