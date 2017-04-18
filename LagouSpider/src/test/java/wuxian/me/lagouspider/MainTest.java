package wuxian.me.lagouspider;

import okhttp3.*;
import org.junit.Test;
import wuxian.me.lagouspider.core.BaseLagouSpider;
import wuxian.me.lagouspider.core.CompanySpider;
import wuxian.me.lagouspider.core.itjuzi.SearchSpider;
import wuxian.me.lagouspider.framework.control.JobMonitor;
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
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.LoggerSpider;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static wuxian.me.lagouspider.util.ModuleProvider.*;

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {

    @Test
    public void testSpiderEquals(){
        IJob job = JobProvider.getJob();
        Area area = new Area();
        area.distinct_name = "123";
        area.name = "3232";
        BaseLagouSpider spider = new AreaSpider(area);
        job.setRealRunnable(spider);
        JobMonitor.getInstance().putJob(job, IJob.STATE_INIT);

        //IJob job1 = JobMonitor.getInstance().getJob(spider);
        //assertTrue(job1 != null);

        assertTrue(JobMonitor.getInstance().contains(job));
        logger().info(job);

        job = JobProvider.getNextJob(job);
        JobMonitor.getInstance().putJob(job, IJob.STATE_RETRY);

        assertTrue(JobMonitor.getInstance().contains(job));
        logger().info(job);

    }

    @Test
    public void testAreaSpider() {
        Config.IS_TEST = true;

        IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        logger().info("Using proxy ip: " + proxy.ip + " port: " + proxy.port);
        ensureIpSwitched(proxy);

        AreaMapper areaMapper = ModuleProvider.areaMapper();
        CompanyMapper companyMapper = ModuleProvider.companyMapper();
        ProductMapper productMapper = ModuleProvider.productMapper();
        LocationMapper locationMapper = ModuleProvider.locationMapper();

        Company.tableName = Helper.getCompanyTableName();
        Product.tableName = Helper.getProductTableName();
        Location.tableName = Helper.getLocationTableName();

        Company company = new Company(-1);
        companyMapper.deleteTable(company);
        companyMapper.createNewTableIfNeed(company);
        companyMapper.createIndex(company);

        Product product = new Product(-1);
        productMapper.deleteTable(product);
        productMapper.createNewTableIfNeed(product);
        productMapper.createIndex(product);

        Location location = new Location(-1, "2r3");
        locationMapper.deleteTable(location);
        locationMapper.createNewTableIfNeed(location);
        locationMapper.createIndex(location);

        logger().info("begin to load area of 西湖区");
        List<Area> areas = areaMapper.loadAreaOfDistinct("西湖区");
        assertTrue(areas.size() != 0);

        for (Area area : areas) {
            logger().info(area.toString());

            IJob job = JobProvider.getJob();
            job.setRealRunnable(LoggerSpider.from(new AreaSpider(area)));
            JobQueue.getInstance().putJob(job);

            break;
        }

        logger().info("Start workThread...");
        WorkThread.getInstance().start();

        while (true) {
            //never stop //http://www.cnblogs.com/yanphet/p/5774291.html
        }
    }

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


        IJob job = JobProvider.getJob();
        job.setRealRunnable(new CompanySpider(37974, ""));
        JobQueue.getInstance().putJob(job);

        logger().info("start workThread...");
        WorkThread.getInstance().start();

        while (true) {

        }
    }

    //Todo
    @Test
    public void testITChengziSearch() {
        Config.IS_TEST = true;
        IPProxyTool.Proxy proxy = IPProxyTool.switchNextProxy();
        logger().info("using proxy ip: " + proxy.ip + " port: " + proxy.port);
        ensureIpSwitched(proxy);

        IJob job = JobProvider.getJob();
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
            logger().info(msg);
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