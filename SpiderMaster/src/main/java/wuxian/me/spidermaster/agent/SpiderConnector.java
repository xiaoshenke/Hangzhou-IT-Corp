package wuxian.me.spidermaster.agent;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import wuxian.me.spidermaster.rpc.RpcDecoder;
import wuxian.me.spidermaster.rpc.RpcEncoder;
import wuxian.me.spidermaster.util.log.LogManager;


/**
 * Created by wuxian on 9/6/2017.
 */
public class SpiderConnector implements Runnable {

    private String host;
    private int port;
    private SocketChannel socketChannel;

    private IConnectCallback connectCallback;

    private IClient client;

    public SpiderConnector(String host, int port, @NotNull IClient client, @Nullable IConnectCallback callback) {
        this.host = host;
        this.port = port;

        this.client = client;
        this.connectCallback = callback;
    }

    public void connectTo(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();  //应该在是当前线程 也就是"ConnectionThread"
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        socketChannel = channel;
                        channel.pipeline()
                                .addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new AgentRpcResponseHandler(client))
                                .addLast(new MasterRpcRequestHandler(client))
                        ;
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future = bootstrap.connect(host, port);//.sync();
        future.awaitUninterruptibly();

        if (future.isCancelled()) {
            LogManager.info("future isCancelled");

            if (connectCallback != null) {
                connectCallback.onFail();
            }
            return;

        } else if (!future.isSuccess()) {
            if (connectCallback != null) {
                connectCallback.onFail();
            }
            LogManager.info("future isFail");
            LogManager.info("cause: " + future.cause().toString());
            return;
        }
        connectCallback.onSuccess(socketChannel);
        try {
            //https://netty.io/4.0/api/io/netty/channel/Channel.html
            //Returns the ChannelFuture which will be notified when this channel is closed.
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {

        } finally {
            group.shutdownGracefully();
        }
    }

    public void run() {
        connectTo(host, port);
    }

}