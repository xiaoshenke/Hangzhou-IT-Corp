package wuxian.me.spidermaster.master;

import com.sun.istack.internal.NotNull;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import wuxian.me.spidermaster.master.core.AgentRpcRequestHandler;
import wuxian.me.spidermaster.rpc.RpcDecoder;
import wuxian.me.spidermaster.rpc.RpcEncoder;
import wuxian.me.spidermaster.util.log.LogManager;

/**
 * Created by wuxian on 18/5/2017.
 */
public class MasterServer {

    private boolean started = false;

    private String host = null;
    private int port;

    public MasterServer(@NotNull String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        if (started) {
            return;
        }
        started = true;

        LogManager.info("MasterServer starting...");

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder())
                                    .addLast(new RpcEncoder())
                                    .addLast(new AgentRpcRequestHandler(socketChannel));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


            LogManager.info("Begin to bind");
            ChannelFuture future = bootstrap.bind(host, port).sync();
            LogManager.info("Bind success");

            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {

        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }
}
