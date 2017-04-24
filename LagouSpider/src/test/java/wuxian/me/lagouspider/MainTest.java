package wuxian.me.lagouspider;

import okhttp3.Request;
import org.junit.Test;
import wuxian.me.lagouspider.core.CompanySpider;
import wuxian.me.lagouspider.core.itjuzi.SearchSpider;
import wuxian.me.lagouspider.framework.BaseSpider;
import wuxian.me.lagouspider.framework.SpiderCallback;
import wuxian.me.lagouspider.framework.control.*;
import wuxian.me.lagouspider.core.AreaSpider;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.mapper.LocationMapper;
import wuxian.me.lagouspider.mapper.ProductMapper;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.framework.IPProxyTool;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.model.Location;
import wuxian.me.lagouspider.model.Product;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.List;

import static org.junit.Assert.*;
import static wuxian.me.lagouspider.Config.JobProvider.FIXED_DELAYJOB_INTERVAL;
import static wuxian.me.lagouspider.Config.JobProvider.USE_FIXED_DELAY_JOB;
import static wuxian.me.lagouspider.util.ModuleProvider.*;

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {

    //Todo
    @Test
    public void testJobMonitPrintFunc() {
        ;
    }

    @Test
    public void testAreaSpider() {
        JobManager manager = JobManager.getInstance();
        IPProxyTool.Proxy proxy = manager.switchProxy();
        logger().info("Using proxy ip: " + proxy.ip + " port: " + proxy.port);
        assertTrue(manager.ipSwitched(proxy, true));

        if (USE_FIXED_DELAY_JOB) {
            logger().info("Current fixed delay job interval: " + FIXED_DELAYJOB_INTERVAL);
        }

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
            IJob job = JobProvider.getJob();
            job.setRealRunnable(new AreaSpider(area));
            manager.putJob(job);
            break;
        }

        logger().info("Start workThread...");
        manager.start();

        while (true) {
            //http://www.cnblogs.com/yanphet/p/5774291.html
        }
    }

    @Test
    public void testWorkThread() {
        JobManager manager = JobManager.getInstance();
        for (int i = 0; i < 20; i++) {
            BaseSpider spider = new DummySpider(i);
            IJob job = JobProvider.getJob();
            job.setRealRunnable(spider);

            manager.putJob(job);
        }
        manager.start();
        while ((true)) {
            ;
        }
    }

    @Test
    public void testCompanyMain() {

        JobManager manager = JobManager.getInstance();
        IPProxyTool.Proxy proxy = manager.switchProxy();
        logger().info("Using proxy ip: " + proxy.ip + " port: " + proxy.port);
        assertTrue(manager.ipSwitched(proxy));

        Company.tableName = Helper.getCompanyTableName();
        Product.tableName = Helper.getProductTableName();
        Location.tableName = Helper.getLocationTableName();

        IJob job = JobProvider.getJob();
        job.setRealRunnable(new CompanySpider(37974, ""));
        JobManager.getInstance().putJob(job);

        logger().info("start workThread...");
        JobManager.getInstance().start();

        while (true) {

        }
    }

    //Todo
    @Test
    public void testITChengziSearch() {
        IJob job = JobProvider.getJob();
        job.setRealRunnable(new SearchSpider(33618, "微贷（杭州）金融信息服务有限公司"));
    }

    private class DummySpider extends BaseSpider {

        private int i;

        public DummySpider(int i) {
            super();
            this.i = i;
        }

        @Override
        public void run() {
            logger().info("run " + name());
        }

        protected SpiderCallback getCallback() {
            return null;
        }

        protected Request buildRequest() {
            return null;
        }

        public int parseRealData(String data) {
            return 0;
        }

        protected boolean checkBlockAndFailThisSpider(String html) {
            return false;
        }

        public String name() {
            return "DummySpider" + i;
        }

        public String fullName() {
            return "DummySpider" + i;
        }
    }
}