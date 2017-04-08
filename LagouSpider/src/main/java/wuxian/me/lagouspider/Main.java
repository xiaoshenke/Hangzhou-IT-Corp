package wuxian.me.lagouspider;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wuxian.me.lagouspider.control.JobMonitor;
import wuxian.me.lagouspider.core.AreaSpider;
import wuxian.me.lagouspider.control.JobProvider;
import wuxian.me.lagouspider.control.JobQueue;
import wuxian.me.lagouspider.control.WorkThread;
import wuxian.me.lagouspider.core.DistinctSpider;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.job.IJob;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.model.Area;

import static wuxian.me.lagouspider.util.FileUtil.*;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 29/3/2017.
 */
@Component
public class Main {

    public static Logger logger = Logger.getLogger(Main.class);
    static {
        PropertyConfigurator.configure(getLog4jPropFilePath());
    }

    AreaMapper areaMapper = ModuleProvider.getInstance().areaMapper;
    CompanyMapper companyMapper = ModuleProvider.getInstance().companyMapper;

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
            if (true || Helper.shouldStartNewGrab()) {     //每过7天开始一次全新抓取
                Helper.updateNewGrab();

                String tableName = Helper.getCompanyTableName();
                companyMapper.createNewTableIfNeed(tableName);

                List<Area> areas = areaMapper.loadAll();
                if (areas == null || areas.size() == 0) {
                    areas = parseAreasFromFile();
                    if (areas.size() == 0) {
                        logger.error("parse Area info from AreaFile error,check your file");
                        return;
                    }
                    insertAreaDataToDB(areas);
                }

                for (Area area : areas) {
                    IJob job = JobProvider.getJob();
                    job.setRealRunnable(new AreaSpider(area));
                    JobQueue.getInstance().putJob(job);

                    JobMonitor.getInstance().putJob(job, IJob.STATE_IN_PROGRESS);
                    if (Helper.isTest) {
                        break;
                    }
                }
                new WorkThread().start();
            }
        }
    }

    public void insertAreaDataToDB(List<Area> areas) {
        for (Area area : areas) {
            areaMapper.insertArea(area.name, area.distinct_name);
        }
    }

    private boolean checkDBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("no jdbc driver");
            return false;
        }

        String url = "jdbc:mysql://127.0.0.1:3306/lagoujob?useUnicode=true&characterEncoding=utf-8";
        String username = "user1";
        String password = "123456";

        try {
            DriverManager.getConnection(url, username, password);
            logger.debug("db check connection success");
            return true;
        } catch (SQLException e) {
            logger.info("db check connection fail");
        }
        return false;
    }

    public static ApplicationContext ctx = null;

    public static void main(String[] args){
        ApplicationContext tmp = new ClassPathXmlApplicationContext("spider.xml");
        ctx = tmp;
        Main main = ctx.getBean(Main.class);
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
