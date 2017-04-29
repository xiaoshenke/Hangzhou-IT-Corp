package wuxian.me.lagouspider;

import okhttp3.Request;
import org.junit.Test;
import wuxian.me.lagouspider.core.itjuzi.SearchSpider;
import wuxian.me.lagouspider.framework.*;
import wuxian.me.lagouspider.framework.control.*;
import wuxian.me.lagouspider.core.AreaSpider;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.mapper.LocationMapper;
import wuxian.me.lagouspider.mapper.ProductMapper;
import wuxian.me.lagouspider.model.Area;
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

    @Test
    public void testAreaSpider() {
        Helper.updateNewGrab();
        JobManager manager = JobManager.getInstance();
        //IPProxyTool.Proxy proxy = manager.switchProxy();
        //logger().info("Using proxy ip: " + proxy.ip + " port: " + proxy.port);
        //assertTrue(manager.ipSwitched(proxy, true));

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
        }

        logger().info("Start workThread...");
        manager.start();

        while (true) {
            //http://www.cnblogs.com/yanphet/p/5774291.html
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

        public String hashString() {
            return "DummySpider" + i;
        }
    }
}