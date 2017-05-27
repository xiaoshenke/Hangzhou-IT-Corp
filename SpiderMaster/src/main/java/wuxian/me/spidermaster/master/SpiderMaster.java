package wuxian.me.spidermaster.master;

import com.sun.istack.internal.NotNull;
import wuxian.me.spidermaster.util.exception.IpPortNotValidException;
import wuxian.me.spidermaster.util.IpPortUtil;

/**
 * Created by wuxian on 27/5/2017.
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
public class SpiderMaster {

    private String serverIp;
    private int serverPort;

    private MasterServer server;

    public SpiderMaster(@NotNull String serverIp, int serverPort) {

        this.serverIp = serverIp;

        this.serverPort = serverPort;

        if (!IpPortUtil.isValidIpPort(serverIp + ":" + serverPort)) {
            throw new IpPortNotValidException();
        }

        this.server = new MasterServer(serverIp, serverPort);
    }

    public void start() {
        this.server.start();
    }
}
