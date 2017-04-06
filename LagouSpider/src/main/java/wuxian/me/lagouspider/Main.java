package wuxian.me.lagouspider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wuxian.me.lagouspider.area.AreaSpider;
import wuxian.me.lagouspider.job.JobProvider;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.job.IJob;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.util.FileUtil;
import wuxian.me.lagouspider.util.Helper;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 29/3/2017.
 */
@Component
public class Main {

    @Autowired
    AreaMapper areaMapper;

    public Main() {
    }

    //Todo:
    public void doJob() {
        if (!checkDBConnection()) {
            System.out.println("connect db fail");
            return;
        }

        if (!HangzhouAreasSpider.areaFileValid()) {      //第一次运行进程的时候先拿到杭州所有的街道信息
            HangzhouAreasSpider spider = new HangzhouAreasSpider();
            spider.beginSpider();

        } else {
            if (true || Helper.shouldStartNewGrab()) {     //每过7天开始一次全新抓取
                Helper.updateNewGrab();
                List<Area> areas = areaMapper.loadAll();
                if (areas == null || areas.size() == 0) {
                    areas = parseAreasFromFile();
                    if (areas.size() == 0) {
                        System.out.println("parese Areas file fail");
                        return;
                    }
                    insertAreaDataToDB(areas);
                }

                for (Area area : areas) {
                    IJob job = JobProvider.getJob();
                    job.setRealRunnable(new AreaSpider(area));
                    JobQueue.getInstance().putJob(job);
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
            System.out.println("area: " + area.name + " distinct: " + area.distinct_name);
            areaMapper.insertArea(area.name, area.distinct_name);
        }
    }

    private static boolean checkDBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No jdbc driver");
            e.printStackTrace();
            return false;
        }

        String url = "jdbc:mysql://127.0.0.1:3306/lagoujob?useUnicode=true&characterEncoding=utf-8";
        String username = "user1";
        String password = "123456";

        try {
            DriverManager.getConnection(url, username, password);
            System.out.println("Database connect success!");
            return true;
        } catch (SQLException e) {
            System.out.println("Database connect failure!");
            e.printStackTrace();
        }
        return false;
    }

    private static ApplicationContext ctx;
    public static void main(String[] args){
        ctx = new ClassPathXmlApplicationContext("spider.xml");

        Main main = ctx.getBean(Main.class);
        main.doJob();
    }

    private List<Area> parseAreasFromFile() {
        List<Area> areaList = new ArrayList<Area>();
        String areaString = FileUtil.readFromFile(FileUtil.getAreaFilePath());
        if (areaString.equals("")) {
            return areaList;
        }
        String[] areas = areaString.split(HangzhouAreasSpider.CUT);

        for (int i = 0; i < areas.length; i++) {
            String[] detail = areas[i].split(HangzhouAreasSpider.SEPRATE);
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
