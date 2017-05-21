package wuxian.me.lagouspider;

import okhttp3.Request;
import org.junit.Test;
import wuxian.me.lagouspider.mapper.boss.BCompanyMapper;
import wuxian.me.lagouspider.mapper.boss.BLocationMapper;
import wuxian.me.lagouspider.mapper.boss.BPositionMapper;
import wuxian.me.lagouspider.model.boss.BCompany;
import wuxian.me.lagouspider.model.boss.BLocation;
import wuxian.me.lagouspider.model.boss.BPosition;
import wuxian.me.lagouspider.util.ModuleProvider;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.SpiderCallback;
import wuxian.me.spidersdk.anti.IPProxyTool;
import wuxian.me.spidersdk.log.LogManager;

import java.sql.DriverManager;
import java.sql.SQLException;

import static wuxian.me.lagouspider.util.ModuleProvider.*;

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {

    @Test
    public void testProxy(){
        String[] ipproxy = new String[]{"115.215.51.170","26964"};
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

}