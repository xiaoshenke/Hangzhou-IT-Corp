package wuxian.me.lagouspider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wuxian.me.lagouspider.area.AreaSpider;
import wuxian.me.lagouspider.job.JobProvider;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.model.Distinct;
import wuxian.me.lagouspider.job.IJob;
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

    public void insertData(List<Area> areas) {
        if (Helper.isTest) {
            for (Area area : areas) {
                areaMapper.insertArea(area.getAreaName(), area.getDistinctName());
            }

            return;
        }
    }

    private static boolean testDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No jdbc driver");
            e.printStackTrace();
            return false;
        }

        String url = "jdbc:mysql://192.168.13.212:3306/lagoujob?useUnicode=true&characterEncoding=utf-8";
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

        if (!HangzhouAreasSpider.areaFileValid()) {
            HangzhouAreasSpider spider = new HangzhouAreasSpider();
            spider.beginSpider();
        } else {
            if (Helper.shouldStartNewGrab()) {
                FileUtil.writeToFile(FileUtil.getGrabFilePath(), String.valueOf(System.currentTimeMillis()));

                List<Area> areas = parseAreasFromFile();
                if (areas.size() == 0) {
                    System.out.println("parese Areas file fail");
                    return;
                }

                if (Helper.isTest) {
                    main.insertData(areas);  //Fixme:现在字符编码有点问题
                    return;
                }

                for (Area area : areas) {
                    IJob job = JobProvider.getJob();
                    job.setRealRunnable(new AreaSpider(area));
                    job.run();

                    if (Helper.isTest) {  //先抓一个试试
                        break;
                    }
                }
            }
        }
    }

    private static List<Area> parseAreasFromFile() {
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
            areaList.add(new Area(new Distinct(detail[0]), detail[1]));
        }
        return areaList;
    }
}
