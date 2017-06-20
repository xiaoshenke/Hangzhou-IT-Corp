package wuxian.me.proxyspider;

import wuxian.me.proxyspider.xun.XunData;
import wuxian.me.proxyspider.xun.XunProxyPool;
import wuxian.me.spidercommon.model.Proxy;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wuxian on 20/6/2017.
 */
public class ProxyProvider {

    private ProxyProvider() {
    }

    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static Proxy getProxy() {

        XunData xunData = XunProxyPool.getXunProxy();
        if (xunData == null) {
            return null;
        }

        return new Proxy(xunData.ip, Integer.parseInt(xunData.port));
    }

    //当池子里没有proxy时 阻塞等着
    public static Proxy getProxySync() {
        Proxy proxy;

        while ((proxy = getProxy()) == null) {
            lock.lock();
            try {
                condition.awaitUninterruptibly();
            } finally {
                lock.unlock();
            }
        }

        return proxy;
    }

    public static void notifyNewProxy() {

        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }

    }
}
