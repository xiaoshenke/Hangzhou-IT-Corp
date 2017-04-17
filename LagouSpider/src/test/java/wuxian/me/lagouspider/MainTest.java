package wuxian.me.lagouspider;

import okhttp3.*;
import org.junit.Test;
import wuxian.me.lagouspider.core.CompanySpider;
import wuxian.me.lagouspider.core.itjuzi.SearchSpider;
import wuxian.me.lagouspider.framework.control.JobProvider;
import wuxian.me.lagouspider.framework.control.JobQueue;
import wuxian.me.lagouspider.framework.control.WorkThread;
import wuxian.me.lagouspider.core.AreaSpider;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.mapper.LocationMapper;
import wuxian.me.lagouspider.mapper.ProductMapper;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.framework.IPProxyTool;
import wuxian.me.lagouspider.framework.OkhttpProvider;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.model.Location;
import wuxian.me.lagouspider.model.Product;
import wuxian.me.lagouspider.save.CompanySaver;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;

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
        //IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        //logger().info("using proxy ip: " + proxy.ip + " port: " + proxy.port);
        //ensureIpSwitched(proxy);

        String tableName = Helper.getCompanyTableName();

        Company.tableName = tableName;
        Product.tableName = Helper.getProductTableName();
        Location.tableName = Helper.getLocationTableName();

        ProductMapper productMapper = ModuleProvider.productMapper();
        LocationMapper locationMapper = ModuleProvider.locationMapper();

        logger().info("begin create product table");
        productMapper.deleteTable(new Product(-1));
        productMapper.createNewTableIfNeed(new Product(-1));

        logger().info("begin create location table");
        locationMapper.deleteTable(new Location(-1, "11"));
        locationMapper.createNewTableIfNeed(new Location(-1, "11"));


        IJob job = JobProvider.getFixedDelayJob(0);
        job.setRealRunnable(new CompanySpider(37974, ""));
        JobQueue.getInstance().putJob(job);

        logger().info("start workThread...");
        WorkThread.getInstance().start();

        while (true) {

        }
    }


    //疑似拉勾会爬取代理网站的ip,若是则立马进行屏蔽...
    @Test
    public void testAreaSpider() {
        Config.IS_TEST = true;

        String tableName = Helper.getCompanyTableName();
        logger().info("TableName: " + tableName);
        Company.tableName = tableName;
        CompanyMapper companyMapper = ModuleProvider.companyMapper();
        companyMapper.createNewTableIfNeed(new Company(-1));

        //IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        //logger().info("Using proxy ip: " + proxy.ip + " port: " + proxy.port);
        //ensureIpSwitched(proxy);

        AreaMapper areaMapper = areaMapper();
        logger().info("begin to load area of 西湖区");
        List<Area> areas = areaMapper.loadAreaOfDistinct("西湖区");
        assertTrue(areas.size() != 0);

        for (Area area : areas) {
            logger().info(area.toString());

            IJob job = JobProvider.getFixedDelayJob();
            job.setRealRunnable(new AreaSpider(area));
            JobQueue.getInstance().putJob(job);

            logger().info("BEGIN AreaSpider " + area.toString());
            break;
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

    //Todo
    @Test
    public void testITChengziSearch() {
        Config.IS_TEST = true;
        IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        logger().info("using proxy ip: " + proxy.ip + " port: " + proxy.port);
        ensureIpSwitched(proxy);

        IJob job = JobProvider.getFixedDelayJob(0);
        job.setRealRunnable(new SearchSpider(33618, "微贷（杭州）金融信息服务有限公司"));
        JobQueue.getInstance().putJob(job);

        logger().info("start workThread...");
        WorkThread.getInstance().start();

        while (true) {
        }
    }


    private void ensureIpSwitched(IPProxyTool.Proxy proxy) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://www.ip138.com/ip2city.asp").newBuilder();
        Headers.Builder builder = new Headers.Builder();
        //builder.add("Connection","close");

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

}