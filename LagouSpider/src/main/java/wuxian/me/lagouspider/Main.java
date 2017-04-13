package wuxian.me.lagouspider;

import wuxian.me.lagouspider.framework.control.JobMonitor;
import wuxian.me.lagouspider.core.AreaSpider;
import wuxian.me.lagouspider.framework.control.JobProvider;
import wuxian.me.lagouspider.framework.control.JobQueue;
import wuxian.me.lagouspider.framework.control.WorkThread;
import wuxian.me.lagouspider.core.DistinctSpider;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.framework.job.IJob;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.model.Area;
import static wuxian.me.lagouspider.util.FileUtil.*;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

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

    public Main() {
    }

    public void run() {
        if (!checkDBConnection()) {
            return;
        }

        if (!DistinctSpider.areaFileValid()) {      //第一次运行进程的时候先拿到杭州所有的街道信息
            DistinctSpider spider = new DistinctSpider();
            spider.beginSpider();
        } else {
            if (Helper.shouldStartNewGrab()) {     //每过7天开始一次全新抓取
                logger().info("begin a new total grab");
                Helper.updateNewGrab();

                logger().info("create new company table");
                String tableName = Helper.getCompanyTableName();
                companyMapper.createNewTableIfNeed(tableName);

                logger().info("load areas from database");
                List<Area> areas = areaMapper.loadAll();
                if (areas == null || areas.size() == 0) {
                    areas = parseAreasFromFile();
                    if (areas.size() == 0) {
                        return;
                    }
                    insertAreaData(areas);
                    areas = areaMapper.loadAll();
                }

                logger().info("add job to jobqueue...");
                for (Area area : areas) {
                    IJob job = JobProvider.getJob();
                    job.setRealRunnable(new AreaSpider(area));
                    JobQueue.getInstance().putJob(job);

                    JobMonitor.getInstance().putJob(job, IJob.STATE_INIT);
                }

                WorkThread.getInstance().start();
                logger().info("start workThread...");
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

    public static void main(String[] args){
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
