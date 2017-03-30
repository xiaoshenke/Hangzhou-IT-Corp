package wuxian.me.lagouspider;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import wuxian.me.lagouspider.area.AreaSpider;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.model.Distinct;
import wuxian.me.lagouspider.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 29/3/2017.
 */
public class Main {

    public static void main(String[] args){
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource res = resolver.getResource("classpath:spider.xml");  //p75
        BeanFactory bf = new XmlBeanFactory(res);
        //bf.getBean("sqlSessionFactory");

        if (!HangzhouAreasSpider.areaFileValid()) {
            HangzhouAreasSpider spider = new HangzhouAreasSpider();
            spider.beginSpider();
        } else {
            //Todo：检查上一次爬虫爬取的时间 若超过xxx时间 再进行爬取

            List<Area> areas = parseAreasFromFile();
            if (areas.size() == 0) {
                System.out.println("parese Areas file fail");
            }

            for (Area area : areas) {
                new AreaSpider(area).beginSpider();
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
