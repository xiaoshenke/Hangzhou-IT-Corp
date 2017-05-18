package wuxian.me.lagouspider;

import wuxian.me.lagouspider.biz.boss.BPositionListSpider;
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
import static wuxian.me.lagouspider.util.Helper.getLagouAreaFilePath;
import static wuxian.me.lagouspider.util.Helper.getLagouDistinctsFilePath;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;
import static wuxian.me.spidersdk.util.FileUtil.readFromFile;

import wuxian.me.lagouspider.model.lagou.Company;
import wuxian.me.lagouspider.model.lagou.Location;
import wuxian.me.lagouspider.model.lagou.Product;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;
import wuxian.me.spidersdk.JobManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.distribute.ClassHelper;
import wuxian.me.spidersdk.distribute.SpiderMethodManager;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.log.ILog;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.util.FileUtil;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wuxian on 29/3/2017.
 */
public class Main {

    static {
        if (!JobManagerConfig.jarMode) {  //IDE运行
            File file = new File("");
            FileUtil.setCurrentPath(file.getAbsolutePath());
        } else {   //JAR包运行
            try{
                File file = new File(Main.class.getProtectionDomain().getCodeSource()
                        .getLocation().toURI().getPath());
                FileUtil.setCurrentFile(file.getAbsolutePath());
                FileUtil.setCurrentPath(file.getParentFile().getAbsolutePath());
            } catch (Exception e){

            }
        }

        JobManager.initCheckFilter(new ClassHelper.CheckFilter() {  //Fix 有的jar包里的类无法加载的问题
            @Override
            public boolean apply(String s) {
                boolean ret = true;
                if(s.contains("org/")){
                    ret = false;
                } else if(s.contains("google")){
                    ret = false;
                }

                return ret;
            }
        });

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
        if (!checkDBConnection()) {
            return;
        }

        if (true) {
            LogManager.error("hello_world");
            return;
        }

        if (true){
            Set<Class> classSet = SpiderMethodManager.getSpiderClasses();
            if(classSet != null){
                for(Class clazz:classSet){
                    LogManager.info("find class: " + clazz);
                }
            }

            IJob job = JobProvider.getJob();
            BPositionListSpider spider = new BPositionListSpider("hellor",1);
            job.setRealRunnable(spider);
            JobManager.getInstance().putJob(job);

            try{
                Thread.sleep(1000);
            } catch (Exception e){

            }

            job = JobManager.getInstance().getJob();

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
            JobManager.getInstance().putJob(job);

        } else if (!FileUtil.checkFileExist(getLagouAreaFilePath())) {
            String distincts = readFromFile(getLagouDistinctsFilePath());
            if (null == distincts) {
                return;
            }
            String[] dis = distincts.split(CUT);  //编码问题带来分解失败...
            for (int i = 0; i < dis.length; i++) {
                DistinctSpider spider = new DistinctSpider(LagouConfig.CITY_TO_SPIDER, dis[i]);
                IJob job = JobProvider.getJob();
                job.setRealRunnable(spider);
                JobManager.getInstance().putJob(job);
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
                IJob job = JobProvider.getJob();
                job.setRealRunnable((new AreaSpider(area)));
                JobManager.getInstance().putJob(job);
            }
        }

        JobManager.getInstance().start();
    }

    private void insertAreaData(List<Area> areas) {
        for (Area area : areas) {
            areaMapper.insertArea(area.name, area.distinct_name);
        }
    }

    private boolean checkDBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LogManager.error("no jdbc driver");
            return false;
        }

        String url = "jdbc:mysql://127.0.0.1:3306/lagoujob?useUnicode=true&characterEncoding=utf-8";
        String username = "user1";
        String password = "123456";

        try {
            DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            LogManager.error("db check connection fail");
        }
        return false;
    }

    public static void main(String[] args) {
        Main main = new Main();
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
