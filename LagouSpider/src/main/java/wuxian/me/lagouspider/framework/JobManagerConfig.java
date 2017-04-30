package wuxian.me.lagouspider.framework;

/**
 * Created by wuxian on 30/4/2017.
 */
public class JobManagerConfig {

    public boolean isTest = false;

    public long okhttpClientSocketReadTimeout = 10 * 1000; //10s

    public String shellOpenProxyFile = "/shell/openproxy";

    public String shellCheckprocessFile = "/shell/processexist";

    public long shellCheckProxyFileSleepTime = 1000 * 10;

    public int proxyHeartbeatInterval = 5 * 1000; //proxy的心跳频率 暂定5秒好了;

    public String ipproxyFile = "/conf/ipproxy.txt";

    public boolean enableSwitchProxy = true;

    public boolean enableRuntimeInputProxy = true;

    public boolean enableInitProxyFromFile = false;

    public int everyProxyTryTime = 4;

    public boolean enableRadomInsertJob = false;

    public boolean enableInsertDuplicateJob = false;

    public boolean enableScheduleImmediately = false;

    public long jobQueueEmptySleepTime = 1000 * 10;

    public int jobNumToSleep = 10;  //每10个job休息一下 降并发

    public int jobSleepTimeToSleep = 1000 * 20; //每x个任务休息xs

    public int jobSchedulerTimeMin = 4;        //每个任务最小相隔xs

    public int jobSchedulerTimeMax = 12;

    public int considerBlockedBlockNum = 1;

    public int considerBlocked404Num = 1;

    public int considerBlockedMayblockNum = 3;

    public int considerBlockedNeterr = 10;

    public boolean enableRetrySpider = true;

    public int singleJobMaxFailTimes = 4;

    public String fulllogFile = "/htmls/";

    public String fulllogPost = ".html";

    //Todo: 从指定路径读入configuration
    private void readConfigFromFile() {
        ;
    }
}
