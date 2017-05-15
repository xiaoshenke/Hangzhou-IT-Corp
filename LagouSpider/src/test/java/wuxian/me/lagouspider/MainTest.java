package wuxian.me.lagouspider;

import okhttp3.Request;
import org.junit.Test;
import wuxian.me.lagouspider.biz.boss.BDisdinctSpider;
import wuxian.me.lagouspider.biz.boss.BPositionListSpider;
import wuxian.me.lagouspider.biz.boss.BPositonDetailSpider;
import wuxian.me.lagouspider.biz.lagou.CompanySpider;
import wuxian.me.lagouspider.biz.lagou.LagouConfig;
import wuxian.me.lagouspider.biz.lagou.DistinctSpider;
import wuxian.me.lagouspider.biz.lagou.AreaSpider;
import wuxian.me.lagouspider.biz.tianyancha.SearchSpider;
import wuxian.me.lagouspider.biz.zhishu.ZhishuSpider;
import wuxian.me.lagouspider.mapper.boss.BCompanyMapper;
import wuxian.me.lagouspider.mapper.boss.BLocationMapper;
import wuxian.me.lagouspider.mapper.boss.BPositionMapper;
import wuxian.me.lagouspider.mapper.lagou.AreaMapper;
import wuxian.me.lagouspider.mapper.lagou.CompanyMapper;
import wuxian.me.lagouspider.mapper.lagou.LocationMapper;
import wuxian.me.lagouspider.mapper.lagou.ProductMapper;
import wuxian.me.lagouspider.model.boss.BCompany;
import wuxian.me.lagouspider.model.boss.BLocation;
import wuxian.me.lagouspider.model.boss.BPosition;
import wuxian.me.lagouspider.model.lagou.Area;
import wuxian.me.lagouspider.model.lagou.Company;
import wuxian.me.lagouspider.model.lagou.Location;
import wuxian.me.lagouspider.model.lagou.Product;
import wuxian.me.lagouspider.util.Config;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.JobManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.SpiderCallback;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.util.FileUtil;


import java.net.URLEncoder;
import java.util.List;

import static org.junit.Assert.*;
import static wuxian.me.lagouspider.util.ModuleProvider.*;
import static wuxian.me.lagouspider.util.Helper.*;

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {

    @Test
    public void testPath() {
        System.out.println(Helper.getCurrentPath());
    }

    @Test
    public void testDate() {
        System.out.println(BPositonDetailSpider.formatPositionPostTime("发布于昨天"));

        System.out.println(BPositonDetailSpider.formatPositionPostTime("发布于14:11"));

        System.out.println(BPositonDetailSpider.formatPositionPostTime("发布于05月10日"));
    }

    @Test
    public void testDBs() {
        BCompanyMapper bCompanyMapper = ModuleProvider.bCompanyMapper();
        BCompany company = new BCompany();
        bCompanyMapper.createNewTableIfNeed(company);
        bCompanyMapper.createIndex(company);

        BLocationMapper bLocationMapper = ModuleProvider.bLocationMapper();
        BLocation location = new BLocation();
        bLocationMapper.createNewTableIfNeed(location);
        bLocationMapper.createIndex(location);

        BPositionMapper bPositionMapper = ModuleProvider.bPositionMapper();
        BPosition position = new BPosition();
        bPositionMapper.createNewTableIfNeed(position);
        bPositionMapper.createIndex(position);

    }

    @Test
    public void testBossPositionDetailSpider() {

        JobManagerConfig.enableScheduleImmediately = true;
        JobManager jobManager = JobManager.getInstance();

        BPositonDetailSpider searchSpider = new BPositonDetailSpider(1411498876);
        Helper.dispatchSpider(searchSpider);
        jobManager.start();

        while (true) {

        }
    }

    @Test
    public void testBossPositionListSpider() {
        JobManagerConfig.enableScheduleImmediately = true;
        JobManager jobManager = JobManager.getInstance();

        String distinctList = FileUtil.readFromFile(Helper.getBossDistinctsFilePath());

        String[] list = distinctList.split(Config.CUT);


        BPositionListSpider searchSpider = new BPositionListSpider(list[0], 1);
        Helper.dispatchSpider(searchSpider);
        jobManager.start();

        while (true) {

        }
    }

    @Test
    public void testBossDistinctSpider() {
        JobManagerConfig.enableScheduleImmediately = true;
        JobManager jobManager = JobManager.getInstance();
        BDisdinctSpider searchSpider = new BDisdinctSpider();
        Helper.dispatchSpider(searchSpider);
        jobManager.start();

        while (true) {

        }
    }

    @Test
    public void testZhishu() {
        JobManagerConfig.enableScheduleImmediately = true;
        JobManager jobManager = JobManager.getInstance();
        ZhishuSpider searchSpider = new ZhishuSpider("美丽说");
        Helper.dispatchSpider(searchSpider);
        jobManager.start();

        while (true) {

        }
    }

    @Test
    //Todo:!
    public void testEncode() {
        String code = "美丽说";
        try {
            byte[] bytes = code.getBytes("unicode");

            String data = URLEncoder.encode(code, "gb2312");
            System.out.println(data);
            System.out.println(data.replace("%", ""));
        } catch (Exception e) {

        }

    }

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

        String content = FileUtil.readFromFile(Helper.getLagouDistinctsFilePath());
        String[] list = content.split(Config.CUT);

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

        Company.tableName = Helper.getLagouCompanyTableName();
        Product.tableName = Helper.getLagouProductTableName();
        Location.tableName = Helper.getLagouLocationTableName();

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

        Company.tableName = Helper.getLagouCompanyTableName();
        Product.tableName = Helper.getLagouProductTableName();
        Location.tableName = Helper.getLagouLocationTableName();

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