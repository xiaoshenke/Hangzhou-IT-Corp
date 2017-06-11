package wuxian.me.spidermaster.master.agentcontroll;

import io.netty.channel.socket.SocketChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 26/5/2017.
 * <p>
 *
 * 事实上在停止一个agent的时候,不一定要停止这个agent上所有的spider。因为Spider可能爬的是不同的网站。
 * 在这里进行简单处理。
 *
 */
public class Agent {

    //该agent的状态变化表
    private List<StatusEnum> statusList = new ArrayList<StatusEnum>();

    //当前agent的状态
    private StatusEnum currentState = null;

    //当前使用的代理
    private Proxy currentProxy = null;

    //当前运行在的channel
    private SocketChannel channel;

    //当前agent上工作着哪些spider
    private List<Spider> spiderList = new ArrayList<Spider>();
}
