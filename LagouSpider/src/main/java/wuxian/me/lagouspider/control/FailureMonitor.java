package wuxian.me.lagouspider.control;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.job.IJob;
import wuxian.me.lagouspider.util.IPProxyTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static wuxian.me.lagouspider.Config.FAIL_FREQUENCY_NETWORK_ERR;
import static wuxian.me.lagouspider.Config.FAIL_FREQUENCY_SERVER_ERR;

/**
 * Created by wuxian on 9/4/2017.
 * 根据失败的情况判断是否ip被屏蔽了:
 * 目前只根据fail的频率 不去管job自身的状态 --> 短期大量的失败认为被屏蔽了 或者网络条件很差
 * Fixme:后续的算法可以进行改进
 */
public class FailureMonitor {

    private FailureMonitor() {
    }

    private static FailureMonitor instance;

    public static FailureMonitor getInstance() {
        if (instance == null) {
            instance = new FailureMonitor();
        }
        return instance;
    }

    private AtomicLong firstFailTime = new AtomicLong(-1);
    private AtomicLong currentFailTime;
    private AtomicInteger networkErrCount = new AtomicInteger(0);

    List<Fail> failList = Collections.synchronizedList(new ArrayList<Fail>());

    //Fixme:暂时先这么实现
    public void success(@NotNull IJob job) {
        ;
    }

    //Fixme:暂时先这么实现
    public void fail(@NotNull IJob job, @NotNull Fail fail) {
        if (firstFailTime.get() == -1) {
            firstFailTime = new AtomicLong(fail.millis);
        }
        currentFailTime = new AtomicLong(fail.millis);

        if (fail.httpCode == Fail.FAIL_NETWORK_ERROR) {
            networkErrCount.set(networkErrCount.get() + 1);
        } else {
            failList.add(fail);
        }

        if (shouldSwitchProxy()) {
            reset();
            IPProxyTool.switchNextProxy();
        }
    }

    private void reset() {
        networkErrCount.set(0);
        firstFailTime.set(-1);
        failList.clear();
    }

    private boolean shouldSwitchProxy() {
        if (currentFailTime.get() - firstFailTime.get() < 1000 * 60) {  //小于1分钟跳过
            return false;
        }

        int minute = (int) (currentFailTime.get() - firstFailTime.get()) / (1000 * 60);
        if (networkErrCount.get() / minute >= FAIL_FREQUENCY_NETWORK_ERR) {
            return true;
        } else if (failList.size() / minute >= FAIL_FREQUENCY_SERVER_ERR) {
            return true;
        }
        return false;
    }
}
