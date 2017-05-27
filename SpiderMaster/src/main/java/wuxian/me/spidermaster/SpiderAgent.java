package wuxian.me.spidermaster;

import com.sun.istack.internal.NotNull;
import wuxian.me.spidermaster.agent.SpiderClient;
import wuxian.me.spidermaster.exception.IpPortNotValidException;
import wuxian.me.spidermaster.util.Util;

/**
 * Created by wuxian on 27/5/2017.
 */
public class SpiderAgent {

    private String serverIp;
    private int serverPort;

    private SpiderClient spiderClient;

    public SpiderAgent(@NotNull String serverIp, int serverPort) {

        this.serverIp = serverIp;

        this.serverPort = serverPort;

        if (!Util.isValidIpPort(serverIp + ":" + serverPort)) {
            throw new IpPortNotValidException();
        }

        spiderClient = new SpiderClient();
    }

    public void start() {
        spiderClient.asyncConnect(serverIp, serverPort);  //Todo: login success callback
    }
}
