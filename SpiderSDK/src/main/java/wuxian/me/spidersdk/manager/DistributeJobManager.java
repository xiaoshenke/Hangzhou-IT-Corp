package wuxian.me.spidersdk.manager;

import com.sun.istack.internal.NotNull;
import wuxian.me.spidersdk.BaseSpider;
import wuxian.me.spidersdk.IJobManager;
import wuxian.me.spidersdk.JobManagerConfig;
import wuxian.me.spidersdk.anti.Fail;
import wuxian.me.spidersdk.anti.IPProxyTool;
import wuxian.me.spidersdk.control.IQueue;
import wuxian.me.spidersdk.control.RedisJobQueue;
import wuxian.me.spidersdk.distribute.*;
import wuxian.me.spidersdk.job.IJob;
import wuxian.me.spidersdk.util.FileUtil;
import wuxian.me.spidersdk.util.ShellUtil;

import java.io.IOException;
import java.util.Set;
import java.util.jar.JarFile;

/**
 * Created by wuxian on 18/5/2017.
 *
 * 分布式下(且没有身份的)的job manager
 * Todo:
 */
public class DistributeJobManager implements IJobManager{

    private IQueue queue;
    private ClassHelper.CheckFilter checkFilter;

    public void setCheckFilter(@NotNull ClassHelper.CheckFilter filter){
        this.checkFilter = filter;
    }

    public DistributeJobManager(){
        init();
    }

    private void init(){
        ShellUtil.init();

        checkAndColloectSubSpiders();

        queue = new RedisJobQueue();

    }

    /**
     * 收集本jar包下的所有合法的@BaseSpider子类
     */
    private void checkAndColloectSubSpiders() throws MethodCheckException {
        Set<Class<?>> classSet = null;
        if (JobManagerConfig.jarMode) {
            String jarPath = "";
            if (FileUtil.currentFile != null) {
                try {
                    JarFile jar = new JarFile(FileUtil.currentFile);
                    classSet = ClassHelper.getJarFileClasses(jar, null, checkFilter);

                } catch (IOException e) {

                }
            } else {
                try {   //Fixme:当这个jar包被引用时 这段代码不起作用
                    jarPath = FileUtil.class.getProtectionDomain().getCodeSource().
                            getLocation().toURI().getPath();
                    JarFile jar = new JarFile(jarPath);
                    classSet = ClassHelper.getJarFileClasses(jar);

                } catch (Exception e) {
                }
            }
        } else {  //Fixme:当这个jar包被引用时 这段代码不起作用
            try {
                classSet = ClassHelper.getClasses("wuxian.me.spidersdk");
            } catch (IOException e) {
                classSet = null;
            }
        }
        if (classSet == null) {
            return;
        }
        for (Class<?> clazz : classSet) {
            SpiderMethodTuple tuple = SpiderClassChecker.performCheckAndCollect(clazz);
            if (tuple != null) {
                SpiderMethodManager.put(clazz, tuple);
            }
        }

    }

    public boolean ipSwitched(IPProxyTool.Proxy proxy) {
        return false;
    }

    public void success(Runnable runnable) {

    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail) {

    }

    public void fail(@NotNull Runnable runnable, @NotNull Fail fail, boolean retry) {

    }

    public IJob getJob() {
        return null;
    }

    public boolean putJob(@NotNull IJob job) {
        return false;
    }

    public void register(@NotNull BaseSpider spider) {

    }

    public void start() {

    }
}
