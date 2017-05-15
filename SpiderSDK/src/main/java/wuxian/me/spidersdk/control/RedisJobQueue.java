package wuxian.me.spidersdk.control;

import com.google.common.primitives.Ints;
import redis.clients.jedis.Jedis;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.distribute.ClassFileUtil;
import wuxian.me.spidersdk.distribute.MethodCheckException;
import wuxian.me.spidersdk.distribute.RedisServerConnectionException;
import wuxian.me.spidersdk.distribute.SpiderChecker;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.util.FileUtil;
import wuxian.me.spidersdk.util.ShellUtil;

import java.io.IOException;
import java.util.Set;
import java.util.jar.JarFile;

/**
 * Created by wuxian on 10/5/2017.
 * <p>
 * 使用url存入RedisJobQueue的方式,pull的时候加一层解析的方式来实现分布式JobQueue
 */
public class RedisJobQueue implements IQueue {

    private Jedis jedis;

    //Fixme:构造函数抛异常？
    public RedisJobQueue() throws RedisServerConnectionException, MethodCheckException {
        init();

    }

    public void init() throws RedisServerConnectionException, MethodCheckException {
        boolean redisRunning = false;
        try {
            redisRunning = ShellUtil.isRedisServerRunning();
        } catch (IOException e) {
            ;
        }

        if (!redisRunning) {
            throw new RedisServerConnectionException();
        }
        jedis = new Jedis(JobManagerConfig.redisIp,
                Ints.checkedCast(JobManagerConfig.redisPort));

        checkSubSpiders();
    }

    //Todo:若@JobManagerConfig没有设置baseScanner类 那么check所有类
    //Fixme:目前只支持jar包模式运行
    private void checkSubSpiders() throws MethodCheckException {
        String jarPath = "";
        Set<Class<?>> classSet = null;
        try {
            jarPath = FileUtil.class.getProtectionDomain().getCodeSource().
                    getLocation().toURI().getPath();

            JarFile jar = new JarFile(jarPath);
            classSet = ClassFileUtil.getJarFileClasses(jar);


        } catch (Exception e) {
        }

        if (classSet == null) {
            return;
        }
        for (Class<?> clazz : classSet) {
            SpiderChecker.performCheck(clazz);
        }

    }

    public boolean putJob(IJob job, int state) {
        return false;
    }

    public IJob getJob() {
        return null;
    }

    public boolean isEmpty() {
        return false;
    }

    public int getJobNum() {
        return 0;
    }
}
