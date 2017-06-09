package wuxian.me.spidermaster.agent;

import com.sun.istack.internal.Nullable;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import wuxian.me.spidermaster.rpc.RpcResponse;

/**
 * Created by wuxian on 27/5/2017.
 * <p>
 * used to connect to @SpiderMaster
 */
public class AgentRpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private RpcResponse response;
    private final Object obj = new Object();
    private SocketChannel socketChannel;
    private IClient client;

    public AgentRpcResponseHandler(@Nullable IClient client) {
        this.client = client;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                RpcResponse rpcResponse) throws Exception {
        /*
        if(client != null) {
            client.onMessage();
        }
        */
    }

}
