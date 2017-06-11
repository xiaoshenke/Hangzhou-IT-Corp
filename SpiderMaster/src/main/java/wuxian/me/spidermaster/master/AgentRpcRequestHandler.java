package wuxian.me.spidermaster.master;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import wuxian.me.spidermaster.master.biz.IBizHandler;
import wuxian.me.spidermaster.rpc.RpcRequest;

/**
 * Created by wuxian on 11/6/2017.
 */
public class AgentRpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private SocketChannel channel;

    public AgentRpcRequestHandler(SocketChannel channel) {
        this.channel = channel;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {

        IBizHandler handler = BizHandlerRegister.getHandlerBy(request.methodName);
        if (handler != null) {
            handler.handleRequest(request, channel);
        }
    }
}
