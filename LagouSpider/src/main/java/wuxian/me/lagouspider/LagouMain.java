package wuxian.me.lagouspider;

import wuxian.me.lagouspider.biz.lagou.AreaSpider;
import wuxian.me.lagouspider.biz.lagou.CitySpider;
import wuxian.me.lagouspider.biz.lagou.LagouConfig;
import wuxian.me.lagouspider.biz.lagou.DistinctSpider;
import wuxian.me.lagouspider.mapper.lagou.AreaMapper;
import wuxian.me.lagouspider.mapper.lagou.CompanyMapper;
import wuxian.me.lagouspider.mapper.lagou.LocationMapper;
import wuxian.me.lagouspider.mapper.lagou.ProductMapper;
import wuxian.me.lagouspider.model.lagou.Area;


import static wuxian.me.lagouspider.util.Config.CUT;
import static wuxian.me.lagouspider.util.Config.SEPRATE;
import static wuxian.me.lagouspider.util.Helper.dispatchSpider;
import static wuxian.me.lagouspider.util.Helper.getLagouAreaFilePath;
import static wuxian.me.lagouspider.util.Helper.getLagouDistinctsFilePath;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;
import static wuxian.me.spidercommon.util.FileUtil.readFromFile;

import wuxian.me.lagouspider.model.lagou.Company;
import wuxian.me.lagouspider.model.lagou.Location;
import wuxian.me.lagouspider.model.lagou.Product;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;
import wuxian.me.spidercommon.log.ILog;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.util.FileUtil;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.manager.JobManagerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 29/3/2017.
 */
public class LagouMain {

    static {
        LogManager.info("LagouSpider.static_Main");

        LogManager.setRealLogImpl(new ILog() {
            public void debug(String message) {
                logger().debug(message);
            }

            public void info(String message) {
                logger().info(message);
            }

            public void error(String message) {
                logger().error(message);
            }

            public void warn(String message) {
                logger().warn(message);
            }
        });

    }

    AreaMapper areaMapper = ModuleProvider.areaMapper();
    CompanyMapper companyMapper = ModuleProvider.companyMapper();
    ProductMapper productMapper = ModuleProvider.productMapper();
    LocationMapper locationMapper = ModuleProvider.locationMapper();

    public void run() {

        if (true) {
            return;
        }

        if (Helper.shouldStartNewGrab()) {  //创建新表
            Helper.updateNewGrab();

            LogManager.info("Create new Tables");
            Company.tableName = Helper.getLagouCompanyTableName();
            Product.tableName = Helper.getLagouProductTableName();
            Location.tableName = Helper.getLagouLocationTableName();
            Area.tableName = Helper.getLagouAreaTableName();

            Area area = new Area();
            areaMapper.createNewTableIfNeed(area);
            areaMapper.createIndex(area);

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
        }

        if (!FileUtil.checkFileExist(getLagouDistinctsFilePath())) {

            CitySpider citySpider = new CitySpider(LagouConfig.CITY_TO_SPIDER);
            IJob job = JobProvider.getJob();
            job.setRealRunnable(citySpider);
            JobManagerFactory.getJobManager().putJob(job);

        } else if (!FileUtil.checkFileExist(getLagouAreaFilePath())) {
            String distincts = readFromFile(getLagouDistinctsFilePath());
            if (null == distincts) {
                return;
            }
            String[] dis = distincts.split(CUT);  //编码问题带来分解失败...
            for (int i = 0; i < dis.length; i++) {
                DistinctSpider spider = new DistinctSpider(LagouConfig.CITY_TO_SPIDER, dis[i]);
                dispatchSpider(spider);
            }

        } else {
            List<Area> areas = areaMapper.loadAll();
            if (areas == null || areas.size() == 0) {
                areas = parseAreasFromFile();
                if (areas.size() == 0) {
                    return;
                }
                insertAreaData(areas);
                areas = areaMapper.loadAll();
            }

            for (Area area : areas) {

                Helper.dispatchSpider(new AreaSpider(area));
            }
        }

        JobManagerFactory.getJobManager().start();
    }

    private void insertAreaData(List<Area> areas) {
        for (Area area : areas) {
            areaMapper.insertArea(area.name, area.distinct_name);
        }
    }


    public static void main(String[] args) {
        LogManager.info("LagouSpider.LagouMain");
        LagouMain main = new LagouMain();
        main.run();
    }

    private List<Area> parseAreasFromFile() {
        List<Area> areaList = new ArrayList<Area>();
        String areaString = readFromFile(getLagouAreaFilePath());
        if (areaString.equals("")) {
            return areaList;
        }
        String[] areas = areaString.split(CUT);

        for (int i = 0; i < areas.length; i++) {
            String[] detail = areas[i].split(SEPRATE);
            if (detail.length != 2) {
                continue;
            }
            Area area = new Area();
            area.name = detail[1];
            area.distinct_name = detail[0];
            areaList.add(area);
        }
        return areaList;
    }
}
