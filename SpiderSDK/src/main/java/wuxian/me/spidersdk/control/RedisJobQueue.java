package wuxian.me.spidersdk.control;

import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.distribute.*;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.log.LogManager;
import wuxian.me.spidersdk.util.ShellUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by wuxian on 10/5/2017.
 * <p>
 * 使用url存入RedisJobQueue的方式,pull的时候加一层解析的方式来实现分布式JobQueue
 */
public class RedisJobQueue implements IQueue {

    private static final String JOB_QUEUE = "jobqueue";

    //HttpUrlNode pattern --> Class
    private Map<Long, Class> urlPatternMap = new HashMap<Long, Class>();
    private List<Long> unResolveList = new ArrayList<Long>();

    private Jedis jedis;
    private Gson gson;


    public RedisJobQueue() {
        init();
    }

    public void init() {
        gson = new Gson();
        boolean redisRunning = false;
        try {
            redisRunning = ShellUtil.isRedisServerRunning();
        } catch (IOException e) {
            ;
        }
        LogManager.info("Check RedisServer running: " + redisRunning);

        if (!redisRunning) {
            throw new RedisConnectionException();
        }

        LogManager.info("Init Jedis client...");
        jedis = new Jedis(JobManagerConfig.redisIp,
                Ints.checkedCast(JobManagerConfig.redisPort));

        LogManager.info("RedisJobQueue Inited");

    }

    //抛弃state --> 分布式下没法管理一个job的状态:是新开始的任务还是重试的任务
    public boolean putJob(IJob job, int state) {
        BaseSpider spider = (BaseSpider) job.getRealRunnable();
        HttpUrlNode urlNode = spider.toUrlNode();
        jedis.lpush(JOB_QUEUE, gson.toJson(urlNode));
        return true;
    }

    public IJob getJob() {
        String spiderStr = jedis.rpop(JOB_QUEUE);
        if (spiderStr == null) {
            return null;
        }

        HttpUrlNode node = gson.fromJson(spiderStr, HttpUrlNode.class);

        long hash = node.hash();
        if (!urlPatternMap.containsKey(hash)) {
            if (unResolveList.contains(hash)) {  //避免多次调用getHandleableClassOf
                return getJob();
            }

            Class clazz = getHandleableClassOf(node);
            if (clazz == null) {
                unResolveList.add(hash);

                jedis.lpush(JOB_QUEUE, spiderStr);
                return getJob();  //重新拿一个呗
            } else {
                urlPatternMap.put(hash, clazz);
            }
        }

        Method fromUrl = SpiderMethodManager.getFromUrlMethod(urlPatternMap.get(hash));
        try {
            BaseSpider spider = (BaseSpider) fromUrl.invoke(node);

            IJob job = JobProvider.getJob();
            job.setRealRunnable(spider);

            return job;
        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        }

        return null;
    }


    private Class getHandleableClassOf(HttpUrlNode node) {
        for (Class clazz : SpiderMethodManager.getSpiderClasses()) {
            Method fromUrl = SpiderMethodManager.getFromUrlMethod(clazz);

            try {
                BaseSpider spider = (BaseSpider) fromUrl.invoke(node);

                if (spider != null) {
                    return clazz;
                } else {
                    continue;
                }
            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        }

        return null;
    }

    public boolean isEmpty() {
        return getJobNum() == 0;
    }

    public int getJobNum() {
        return Ints.checkedCast(jedis.llen(JOB_QUEUE));
    }

}
