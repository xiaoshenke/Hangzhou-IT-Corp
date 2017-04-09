package wuxian.me.lagouspider;

import okhttp3.*;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.junit.Test;
import wuxian.me.lagouspider.control.JobMonitor;
import wuxian.me.lagouspider.control.JobProvider;
import wuxian.me.lagouspider.control.JobQueue;
import wuxian.me.lagouspider.control.WorkThread;
import wuxian.me.lagouspider.core.AreaSpider;
import wuxian.me.lagouspider.job.IJob;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.util.IPProxyTool;
import wuxian.me.lagouspider.util.OkhttpProvider;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static wuxian.me.lagouspider.util.ModuleProvider.*;

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {

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

        new WorkThread().start();

        while (true) {
        }
    }

    @Test
    public void testFrequency() {
        Config.IS_TEST = true;

        IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        logger().info("using proxy ip: " + proxy.ip + " port: " + proxy.port);
        ensureIpSwitched(proxy);

        AreaMapper areaMapper = areaMapper();
        List<Area> areas = areaMapper.loadAll();
        assertTrue(areas.size() != 0);

        for (Area area : areas) {
            IJob job = JobProvider.getFixedDelayJob(0);
            job.setRealRunnable(new AreaSpider(area));
            JobQueue.getInstance().putJob(job);

            JobMonitor.getInstance().putJob(job, IJob.STATE_INIT);
        }

        logger().info("start workThread...");
        new WorkThread().start();

        while (true) {
            //never stop //http://www.cnblogs.com/yanphet/p/5774291.html
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
        try {
            Response response = OkhttpProvider.getClient().newCall(request).execute();
            logger().debug("response success: " + response.isSuccessful());
            assertTrue(response.isSuccessful());

            String msg = response.body().string();
            logger().debug(msg);

            assertTrue(msg.contains(proxy.ip));

        } catch (IOException e) {
            logger().error("request fail");  //使用代理会有些不大稳定...
        }
    }

    @Test
    public void testSwitchIp() {
        final IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        logger().info("proxy ip: " + proxy.ip + " port: " + proxy.port);
        ensureIpSwitched(proxy);
    }

}