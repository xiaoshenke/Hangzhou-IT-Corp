package wuxian.me.lagouspider;

/*
import org.junit.Test;
import wuxian.me.lagouspider.biz.boss.BPositionListSpider;
import wuxian.me.lagouspider.biz.boss.BPositonDetailSpider;
import wuxian.me.lagouspider.biz.boss.BaseBossSpider;
import wuxian.me.lagouspider.mapper.boss.BCompanyMapper;
import wuxian.me.lagouspider.mapper.boss.BLocationMapper;
import wuxian.me.lagouspider.mapper.boss.BPositionMapper;
import wuxian.me.lagouspider.model.boss.BCompany;
import wuxian.me.lagouspider.model.boss.BLocation;
import wuxian.me.lagouspider.model.boss.BPosition;
import wuxian.me.lagouspider.util.ModuleProvider;
import wuxian.me.spidersdk.anti.IPProxyTool;
import wuxian.me.spidersdk.distribute.HttpUrlNode;
import wuxian.me.spidersdk.util.FileUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static wuxian.me.lagouspider.util.ModuleProvider.*;
*/

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {
    /*

    @Test
    public void testAnti() {
        Main.init();
        String s = FileUtil.readFromFile(FileUtil.getCurrentPath() + "/anti-spider.html");
        System.out.println(BaseBossSpider.doCheckBlockAndFailThisSpider(s));
    }

    @Test
    public void testParseDetail() {
        String content = FileUtil.readFromFile(FileUtil.getCurrentPath() + "/whole_spider.txt");

        String reg = "(?<=positionId: )[0-9]+";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);

        int i = 0;
        while (matcher.find()) {
            i++;
            System.out.println(matcher.group());
        }

        System.out.println();
        System.out.println(i);
    }

    @Test
    public void testBDetail() {
        Main.init();
        //JobManagerFactory.getJobManager().start();
        BPositonDetailSpider spider = new BPositonDetailSpider(1411652718);
        //Helper.dispatchSpider(spider);
        spider.run();
        while (true) {

        }
    }

    @Test
    public void testBPositionList() {

        HttpUrlNode node = BPositionListSpider.toUrlNode(new BPositionListSpider("西湖", 1));
        BPositionListSpider spider = BPositionListSpider.fromUrlNode(node);

        System.out.println(spider);
    }

    @Test
    public void testBPositionDetail() {
        HttpUrlNode node = BPositonDetailSpider.toUrlNode(new BPositonDetailSpider(3341));
        BPositonDetailSpider spider = BPositonDetailSpider.fromUrlNode(node);
        System.out.println(spider);
    }


    @Test
    public void testList() {
        Main.init();

        new Main().run();

        while (true) {

        }
    }


    @Test
    public void testProxy() {
        String[] ipproxy = new String[]{"115.215.51.170", "26964"};
        System.out.println(IPProxyTool.isVaildIpPort(ipproxy));
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
    */

}