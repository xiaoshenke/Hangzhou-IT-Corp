package wuxian.me.spidermaster;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import wuxian.me.spidermaster.netty.RpcDecoder;
import wuxian.me.spidermaster.netty.RpcEncoder;
import wuxian.me.spidermaster.util.Util;

/**
 * Created by wuxian on 18/5/2017.
 * <p>
 * Todo:
 * <p>
 * 规划的功能
 * 1 登陆 --> 一个可靠的agent必须先登陆到master 登陆时带上自己的一系列信息 比如使用的proxy
 * 此后维护一个可靠的长连接？
 * 2 agent登出功能
 * 3 下发开始工作消息(包括一系列配置 抓取的频率 redis的ip port等)
 * 4 下发暂停工作消息
 * 5 agent上报被屏蔽信息 包括运行了多久,完成了多少任务,成功率,失败率
 * 6 下发resume工作消息(唤起暂停的工作)
 * <p>
 * <p>
 * 7 下发proxy？下发cookie？
 */
public class MasterServer {

    private boolean started = false;

    private String ipPort = "127.0.0.1:2987";

    public void start() {

        if (started) {
            return;
        }

        if (!Util.isValidIpPort(ipPort)) {
            return;
        }

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder())
                                    .addLast(new RpcEncoder());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = ipPort.split(":");
            ChannelFuture future = bootstrap.bind(array[0], Integer.parseInt(array[1])).sync();

            future.channel().closeFuture();
        } catch (InterruptedException e) {

        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }

    public static void main(String[] args) {

        new MasterServer().start();
    }
}
