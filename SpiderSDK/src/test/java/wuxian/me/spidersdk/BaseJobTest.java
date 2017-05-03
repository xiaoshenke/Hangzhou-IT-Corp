package wuxian.me.spidersdk;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import wuxian.me.spidersdk.util.ShellUtil;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by wuxian on 3/5/2017.
 */
public class BaseJobTest {

    @Test
    public void testShell() {
        JobManagerConfig.useRedis = true;

        try {
            System.out.println(ShellUtil.isRedisServerRunning());
        } catch (IOException e) {
            ;
        }
    }

    @Test
    public void testJedis() {
        Jedis jedis;
        jedis = new Jedis("127.0.0.1", 6379);

        jedis.set("name", "xinxin");
        System.out.println(jedis.get("name"));
    }

}