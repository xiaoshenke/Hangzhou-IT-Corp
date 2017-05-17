package wuxian.me.spidersdk.control;

import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.JobManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.distribute.*;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.job.JobProvider;
import wuxian.me.spidersdk.util.FileUtil;
import wuxian.me.spidersdk.util.ShellUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarFile;

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

        if (!redisRunning) {
            throw new RedisConnectionException();
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
        if (FileUtil.currentFile != null) {
            try {
                JarFile jar = new JarFile(FileUtil.currentFile);
                classSet = ClassHelper.getJarFileClasses(jar, null, JobManager.checkFilter);

            } catch (IOException e) {

            }
        } else {
            try {
                jarPath = FileUtil.class.getProtectionDomain().getCodeSource().
                        getLocation().toURI().getPath();
                JarFile jar = new JarFile(jarPath);
                classSet = ClassHelper.getJarFileClasses(jar);

            } catch (Exception e) {
            }
        }


        if (classSet == null) {
            return;
        }
        for (Class<?> clazz : classSet) {
            //收集method
            //System.out.println("check method of: "+clazz);
            SpiderMethodTuple tuple = SpiderClassChecker.performCheckAndCollect(clazz);
            if (tuple != null) {
                SpiderMethodManager.put(clazz, tuple);
            }
        }

    }

    //Todo:state管理
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
        return false;
    }

    public int getJobNum() {
        return 0;
    }

}
