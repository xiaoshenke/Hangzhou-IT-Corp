package wuxian.me.spidermaster.agent;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import wuxian.me.spidermaster.rpc.RpcDecoder;
import wuxian.me.spidermaster.rpc.RpcEncoder;
import wuxian.me.spidermaster.rpc.RpcResponse;
import wuxian.me.spidermaster.rpc.client.RpcClient;
import wuxian.me.spidermaster.util.log.LogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 27/5/2017.
 * <p>
 * used to connect to @SpiderMaster
 */
public class Connector extends SimpleChannelInboundHandler<RpcResponse> implements Runnable {

    private List<ConnectCallback> callbacks = new ArrayList<ConnectCallback>();
    private RpcResponse response;
    private final Object obj = new Object();
    private SocketChannel socketChannel;
    private String host;
    private int port;

    public Connector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //Todo
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                RpcResponse rpcResponse) throws Exception {

    }

    //Todo:
    public void closeConnection() {
        ;
    }

    //Todo: construct request
    public void connectTo(String host, int port) {

        LogManager.info("Connector begin connect to " + host + ":" + port);
        EventLoopGroup group = new NioEventLoopGroup();

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
                                .addLast(Connector.this);
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future = bootstrap.connect(host, port);//.sync();
        future.awaitUninterruptibly();

        if (future.isCancelled()) {

            LogManager.info("future isCancelled");

            for (ConnectCallback cb : callbacks) {
                cb.onFail();
            }
            return;
        } else if (!future.isSuccess()) {
            for (ConnectCallback cb : callbacks) {
                cb.onFail();
            }

            LogManager.info("future isFail");
            LogManager.info("cause: " + future.cause().toString());

            return;
        }
        //future.channel().writeAndFlush(request).sync();  //Todo

        //synchronized (obj) {
        //    obj.wait(); // 未收到响应，使线程等待
        //}
        if (response != null) {
            for (ConnectCallback cb : callbacks) {
                cb.onSuccess(socketChannel);
            }
        }

        try {
            //https://netty.io/4.0/api/io/netty/channel/Channel.html
            //Returns the ChannelFuture which will be notified when this channel is closed.
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {

            for (ConnectCallback cb : callbacks) {
                cb.onException();
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public void register(ConnectCallback cb) {

        if (!callbacks.contains(cb)) {
            callbacks.add(cb);
        }
    }

    public void run() {
        connectTo(host, port);
    }


    public interface ConnectCallback {
        void onSuccess(SocketChannel channel);

        void onFail();

        void onException();
    }
}
