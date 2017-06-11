package wuxian.me.spidermaster.agent;

import com.sun.istack.internal.NotNull;
import wuxian.me.spidercommon.util.IpPortUtil;
import wuxian.me.spidermaster.util.exception.IpPortNotValidException;

/**
 * Created by wuxian on 27/5/2017.
 *
 * Todo:这个类的api设计？
 */
public class SpiderAgent {

    private String serverIp;
    private int serverPort;

    private SpiderClient spiderClient;

    public SpiderAgent(@NotNull String serverIp, int serverPort) {

        this.serverIp = serverIp;

        this.serverPort = serverPort;

        if (!IpPortUtil.isValidIpPort(serverIp + ":" + serverPort)) {
            throw new IpPortNotValidException();
        }

        spiderClient = new SpiderClient();
    }

    public void start() {
        spiderClient.asyncConnect(serverIp, serverPort);
    }
}
