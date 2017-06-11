package wuxian.me.spidermaster;

import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.util.ProcessUtil;
import wuxian.me.spidercommon.util.ShellUtil;
import wuxian.me.spidercommon.util.SignalManager;
import wuxian.me.spidermaster.agent.SpiderAgent;
import wuxian.me.spidermaster.master.SpiderMaster;
import wuxian.me.spidermaster.util.SpiderConfig;

/**
 * Created by wuxian on 18/5/2017.
 */
public class Main {

    private SignalManager signalManager = new SignalManager();

    public void start() {
        SpiderConfig.init();
        ShellUtil.init();

        signalManager.init();
        signalManager.registerOnSystemKill(new SignalManager.OnSystemKill() {
            public void onSystemKilled() {
                LogManager.info("onSystemKilled");

                ShellUtil.killProcessBy(ProcessUtil.getCurrentProcessId());
            }
        });

        if (SpiderConfig.spiderMode == 0) {  //agent
            startAgent();
        } else {  //master
            startMaster();
        }
    }


    private void startAgent() {
        LogManager.info("startAgent...");
        new SpiderAgent(SpiderConfig.masterIp, SpiderConfig.masterPort)
                .start();
    }

    private void startMaster() {
        LogManager.info("startMaster...");
        new SpiderMaster(SpiderConfig.masterIp, SpiderConfig.masterPort)
                .start();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
