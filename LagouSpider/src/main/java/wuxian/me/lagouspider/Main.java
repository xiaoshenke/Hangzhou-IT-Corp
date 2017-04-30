package wuxian.me.lagouspider;

import wuxian.me.lagouspider.core.AreaSpider;
import wuxian.me.lagouspider.framework.control.JobManager;
import wuxian.me.lagouspider.framework.control.JobProvider;
import wuxian.me.lagouspider.core.DistinctSpider;
import wuxian.me.lagouspider.framework.log.ILog;
import wuxian.me.lagouspider.framework.log.LogManager;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.mapper.LocationMapper;
import wuxian.me.lagouspider.mapper.ProductMapper;
import wuxian.me.lagouspider.model.Area;

import static wuxian.me.lagouspider.framework.FileUtil.*;
import static wuxian.me.lagouspider.util.Helper.getAreaFilePath;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.model.Location;
import wuxian.me.lagouspider.model.Product;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 29/3/2017.
 */
public class Main {

    AreaMapper areaMapper = ModuleProvider.areaMapper();
    CompanyMapper companyMapper = ModuleProvider.companyMapper();
    ProductMapper productMapper = ModuleProvider.productMapper();
    LocationMapper locationMapper = ModuleProvider.locationMapper();

    public Main() {

        LogManager.setRealLogImpl(new ILog() {
            public void debug(String message) {
                logger().debug(message);
            }

            public void info(String message) {
                logger().info(message);

            }

            public void error(String message) {
                logger().info(message);

            }

            public void warn(String message) {
                logger().info(message);

            }
        });
    }

    public void run() {
        if (!checkDBConnection()) {
            return;
        }

        if (!DistinctSpider.areaFileValid()) {      //第一次运行进程的时候先拿到杭州所有的街道信息
            DistinctSpider spider = new DistinctSpider();
            spider.beginSpider();
        } else {
            if (Helper.shouldStartNewGrab()) {
                logger().info("Begin a new total grab");
                Helper.updateNewGrab();

                logger().info("Create new Tables");
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

                logger().info("Load areas from database");
                List<Area> areas = areaMapper.loadAll();
                if (areas == null || areas.size() == 0) {
                    areas = parseAreasFromFile();
                    if (areas.size() == 0) {
                        return;
                    }
                    insertAreaData(areas);
                    areas = areaMapper.loadAll();
                }

                logger().info("Add AreaSpider to JobQueue...");
                for (Area area : areas) {
                    IJob job = JobProvider.getJob();
                    job.setRealRunnable((new AreaSpider(area)));
                    JobManager.getInstance().putJob(job);
                }

                logger().info("Start workThread...");
                JobManager.getInstance().start();
            }
        }
    }

    public void insertAreaData(List<Area> areas) {
        for (Area area : areas) {
            areaMapper.insertArea(area.name, area.distinct_name);
        }
    }

    private boolean checkDBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger().error("no jdbc driver");
            return false;
        }

        String url = "jdbc:mysql://127.0.0.1:3306/lagoujob?useUnicode=true&characterEncoding=utf-8";
        String username = "user1";
        String password = "123456";

        try {
            DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            logger().error("db check connection fail");
        }
        return false;
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private List<Area> parseAreasFromFile() {
        List<Area> areaList = new ArrayList<Area>();
        String areaString = readFromFile(getAreaFilePath());
        if (areaString.equals("")) {
            return areaList;
        }
        String[] areas = areaString.split(DistinctSpider.CUT);

        for (int i = 0; i < areas.length; i++) {
            String[] detail = areas[i].split(DistinctSpider.SEPRATE);
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
