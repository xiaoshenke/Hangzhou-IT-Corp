package wuxian.me.spidermaster;

import wuxian.me.spidermaster.util.SpiderConfig;

/**
 * Created by wuxian on 18/5/2017.
 */
public class Main {

    public void start() {
        SpiderConfig.init();

        if (SpiderConfig.spiderMode == 0) {  //agent
            startAgent();
        } else {  //master
            startMaster();
        }
    }


    private void startAgent() {
        new SpiderAgent(SpiderConfig.masterIp, SpiderConfig.masterPort)
                .start();
    }

    private void startMaster() {
        new SpiderMaster(SpiderConfig.masterIp, SpiderConfig.masterPort)
                .start();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
