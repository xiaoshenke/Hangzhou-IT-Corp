package wuxian.me.lagouspider;


import org.junit.Test;
import wuxian.me.lagouspider.biz.lagou.PositionSpider;
import wuxian.me.spidersdk.manager.JobManagerFactory;

/**
 * Created by wuxian on 9/4/2017.
 */
public class MainTest {

    @Test
    public void testSerialize() {
        //JobManagerFactory.getJobManager().start();
        PositionSpider spider = new PositionSpider("西湖", 1);

        System.out.println(PositionSpider.fromUrlNode(spider.toUrlNode()));
    }

}