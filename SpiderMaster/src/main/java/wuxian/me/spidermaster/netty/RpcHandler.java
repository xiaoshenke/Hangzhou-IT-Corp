package wuxian.me.spidermaster.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wuxian.me.spidermaster.rpc.RpcRequest;

/**
 * Created by wuxian on 26/5/2017.
 * <p>
 * Todo: add other handlers
 * <p>
 * Todo: save connections
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {


    //Todo: dispatch request
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        ctx.close();
    }
}
