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
import static wuxian.me.lagouspider.util.ModuleProvider.*;

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {

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
}