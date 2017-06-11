package wuxian.me.spidermaster.master;

import com.sun.istack.internal.NotNull;
import wuxian.me.spidermaster.master.biz.RegisterHandler;
import wuxian.me.spidermaster.master.core.BizHandlerRegister;
import wuxian.me.spidermaster.util.exception.IpPortNotValidException;
import wuxian.me.spidermaster.util.IpPortUtil;

/**
 * Created by wuxian on 27/5/2017.
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
        BizHandlerRegister.registerBizHandler(new RegisterHandler());

        this.server.start();
    }
}
