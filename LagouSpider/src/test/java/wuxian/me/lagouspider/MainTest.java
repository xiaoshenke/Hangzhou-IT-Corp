package wuxian.me.lagouspider;

import okhttp3.Request;
import org.junit.Test;
import wuxian.me.lagouspider.business.lagou.CompanySpider;
import wuxian.me.lagouspider.business.lagou.LagouConfig;
import wuxian.me.lagouspider.business.lagou.DistinctSpider;
import wuxian.me.lagouspider.business.lagou.AreaSpider;
import wuxian.me.lagouspider.business.tianyancha.SearchSpider;
import wuxian.me.lagouspider.mapper.lagou.AreaMapper;
import wuxian.me.lagouspider.mapper.lagou.CompanyMapper;
import wuxian.me.lagouspider.mapper.lagou.LocationMapper;
import wuxian.me.lagouspider.mapper.lagou.ProductMapper;
import wuxian.me.lagouspider.model.lagou.Area;
import wuxian.me.lagouspider.model.lagou.Company;
import wuxian.me.lagouspider.model.lagou.Location;
import wuxian.me.lagouspider.model.lagou.Product;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.JobManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.SpiderCallback;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.util.FileUtil;

import java.util.List;

import static org.junit.Assert.*;
import static wuxian.me.lagouspider.util.ModuleProvider.*;
import static wuxian.me.lagouspider.util.Helper.*;

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {

    //Damn! 页面是用js加载的 还没成功破解
    @Test
    public void testSearchSpider() {
        JobManagerConfig.enableScheduleImmediately = true;
        JobManager jobManager = JobManager.getInstance();
        SearchSpider searchSpider = new SearchSpider(-1, "杭州随地付网络技术有限公司");

        Helper.dispatchSpider(searchSpider);
        jobManager.start();

        while (true) {

        }
    }

    @Test
    public void testCity() {

        String content = FileUtil.readFromFile(Helper.getDistinctsFilePath());
        String[] list = content.split(LagouConfig.CUT);

        for (int i = 0; i < list.length; i++) {
            DistinctSpider spider = new DistinctSpider(LagouConfig.CITY_TO_SPIDER, list[i]);

            dispatchSpider(spider);
        }
        JobManager.getInstance().start();

        while (true) {

        }
    }

    @Test
    public void testCompanySpider() {

        JobManager manager = JobManager.getInstance();
        CompanyMapper companyMapper = ModuleProvider.companyMapper();

        Company.tableName = Helper.getCompanyTableName();
        Product.tableName = Helper.getProductTableName();
        Location.tableName = Helper.getLocationTableName();

        /*
        Company company = new Company(-1);
        companyMapper.createNewTableIfNeed(company);
        Product product = new Product(-1);
        productMapper.createNewTableIfNeed(product);
        Location location = new Location(-1, "2r3");
        locationMapper.createNewTableIfNeed(location);
        */

        List<Company> companyList = companyMapper.loadAllCompanies(Company.tableName);

        for (Company company : companyList) {
            IJob job = JobProvider.getJob();
            job.setRealRunnable(new CompanySpider(company.company_id, company.company_fullname));
            manager.putJob(job);
        }

        logger().info("Start workThread...");
        manager.start();

        while (true) {

        }

    }

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