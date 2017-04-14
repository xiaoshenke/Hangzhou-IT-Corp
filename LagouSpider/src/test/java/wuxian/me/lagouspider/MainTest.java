package wuxian.me.lagouspider;

import okhttp3.*;
import org.junit.Test;
import wuxian.me.lagouspider.core.CompanySpider;
import wuxian.me.lagouspider.framework.control.JobMonitor;
import wuxian.me.lagouspider.framework.control.JobProvider;
import wuxian.me.lagouspider.framework.control.JobQueue;
import wuxian.me.lagouspider.framework.control.WorkThread;
import wuxian.me.lagouspider.core.AreaSpider;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.framework.IPProxyTool;
import wuxian.me.lagouspider.framework.OkhttpProvider;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static wuxian.me.lagouspider.util.ModuleProvider.*;

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {

    //Todo: 切换IP,重试队列的联合测试

    @Test
    public void testCompanyMain() {
        Config.IS_TEST = true;
        IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        logger().info("using proxy ip: " + proxy.ip + " port: " + proxy.port);
        ensureIpSwitched(proxy);

        IJob job = JobProvider.getFixedDelayJob(0);
        job.setRealRunnable(new CompanySpider(43753));
        JobQueue.getInstance().putJob(job);

        logger().info("start workThread...");
        WorkThread.getInstance().start();

        while (true) {

        }
    }

    //Fixme:疑似拉勾会爬取代理网站的ip,若是则立马进行屏蔽...
    @Test
    public void testFrequency() {
        Config.IS_TEST = true;

        IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        logger().info("Using proxy ip: " + proxy.ip + " port: " + proxy.port);
        ensureIpSwitched(proxy);

        AreaMapper areaMapper = areaMapper();
        List<Area> areas = areaMapper.loadAll();
        assertTrue(areas.size() != 0);

        final int AREA_NUM = 5;
        int i = 0;
        for (Area area : areas) {
            if (i >= AREA_NUM) {
                break;
            }
            IJob job = JobProvider.getFixedDelayJob(0);
            job.setRealRunnable(new AreaSpider(area));
            JobQueue.getInstance().putJob(job);

            JobMonitor.getInstance().putJob(job, IJob.STATE_INIT);
            //i++;
        }

        logger().info("Start workThread...");
        WorkThread.getInstance().start();

        while (true) {
            //never stop //http://www.cnblogs.com/yanphet/p/5774291.html
        }
    }

    @Test
    public void testSuspendWorkThread() {
        Config.IS_TEST = true;

        AreaMapper areaMapper = areaMapper();
        List<Area> areas = areaMapper.loadAll();
        assertTrue(areas.size() != 0);

        for (Area area : areas) {
            IJob job = JobProvider.getFixedDelayJob(0);
            job.setRealRunnable(new AreaSpider(area));
            JobQueue.getInstance().putJob(job);

            JobMonitor.getInstance().putJob(job, IJob.STATE_INIT);
        }

        WorkThread thread = WorkThread.getInstance();
        thread.start();
        try {
            Thread.sleep(100);
            logger().info("begin to pauseWhenSwitchIP");
            thread.pauseWhenSwitchIP();

            Thread.sleep(1000);
            logger().info("begin to resume");
            thread.resumeNow();

            Thread.sleep(10000);
        } catch (InterruptedException e) {

        }
    }

    private void ensureIpSwitched(IPProxyTool.Proxy proxy) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://www.ip138.com/ip2city.asp").newBuilder();
        Headers.Builder builder = new Headers.Builder();
        builder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        final Request request = new Request.Builder()
                .headers(builder.build())
                .url(urlBuilder.build().toString())
                .build();
        Response response = null;
        try {
            response = OkhttpProvider.getClient().newCall(request).execute();
            assertTrue(response.isSuccessful());
            String msg = response.body().string();
            logger().debug(msg);
            assertTrue(msg.contains(proxy.ip));
        } catch (IOException e) {
            logger().error("switch ip fail");  //使用代理会有些不大稳定...
            assertTrue(false);
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
    }

    @Test
    public void testSwitchIp() {
        final IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        logger().info("proxy ip: " + proxy.ip + " port: " + proxy.port);
        ensureIpSwitched(proxy);
    }

    @Test
    public void testDelayJob() {
        Config.IS_TEST = true;

        Runnable runnable = new Runnable() {
            public void run() {
                logger().info("real run at: " + System.currentTimeMillis());
            }
        };
        IJob job = JobProvider.getDelayJob(1000 * 2);
        job.setRealRunnable(runnable);

        JobQueue.getInstance().putJob(job);
        logger().info("put runnable at: " + System.currentTimeMillis());

        WorkThread.getInstance().start();

        while (true) {
        }
    }

}