package wuxian.me.lagouspider.util;

import com.sun.istack.internal.NotNull;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wuxian.me.lagouspider.Main;
import wuxian.me.lagouspider.mapper.boss.BCompanyMapper;
import wuxian.me.lagouspider.mapper.boss.BLocationMapper;
import wuxian.me.lagouspider.mapper.boss.BPositionMapper;
import wuxian.me.lagouspider.mapper.lagou.*;
import wuxian.me.spidercommon.log.LogManager;
import wuxian.me.spidercommon.util.FileUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static wuxian.me.lagouspider.util.Helper.getLog4jPropFilePath;
import static wuxian.me.lagouspider.util.Helper.getWriteLogFilePath;

/**
 * Created by wuxian on 8/4/2017
 */
@Component
public class ModuleProvider {

    private static Logger logger = Logger.getLogger(Main.class);
    private static ApplicationContext ctx;
    private static ModuleProvider instance;

    static {
        try {
            ctx = new ClassPathXmlApplicationContext("spider.xml");
        } catch (BeansException e) {
            LogManager.error("BeansException: " + e.getMessage());
        }

        instance = ctx.getBean(ModuleProvider.class);
        PropertyConfigurator.configure(getLog4jPropFilePath());
        try {
            DailyRollingFileAppender appender = (DailyRollingFileAppender) logger.getRootLogger().getAppender("E");
            appender.setFile(getWriteLogFilePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        BasicDataSource dataSource = ctx.getBean(BasicDataSource.class);
        reInitDataSouce(dataSource);

    }

    private static void reInitDataSouce(BasicDataSource dataSource) {
        if (dataSource == null) {
            return;
        }

        Properties pro = new Properties();
        FileInputStream in = null;
        boolean success = false;
        try {
            in = new FileInputStream(FileUtil.getCurrentPath()
                    + "/conf/dataSource.properties");
            pro.load(in);
            success = true;
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            ;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    ;
                }

            }
        }

        if (!success) {
            pro = null; //确保一定会初始化
        }

        String url = parse(pro, "jdbc.url", null);
        if (url != null) {
            dataSource.setUrl(url);
        }

        String userName = parse(pro, "jdbc.username", null);
        if (userName != null) {
            dataSource.setUsername(userName);
        }

        String pwd = parse(pro, "jdbc.password", null);
        if (pwd != null) {
            dataSource.setPassword(pwd);
        }
    }

    private static String parse(@NotNull Properties pro, String key, String defValue) {
        if (pro == null) {
            return defValue;
        }

        try {
            return pro.getProperty(
                    key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }


    private ModuleProvider() {
    }

    public static Logger logger() {
        return logger;
    }

    public static AreaMapper areaMapper() {
        return instance.areaMapper;
    }

    public static CompanyMapper companyMapper() {
        return instance.companyMapper;
    }

    public static ProductMapper productMapper() {
        return instance.productMapper;
    }

    public static LocationMapper locationMapper() {
        return instance.locationMapper;
    }

    public static BPositionMapper bPositionMapper() {
        return instance.bpositionMapper;
    }

    public static BCompanyMapper bCompanyMapper() {
        return instance.bcompanyMapper;
    }

    public static BLocationMapper bLocationMapper() {
        return instance.blocationMapper;
    }

    public static PositionMapper positionMapper() {
        return instance.positionMapper;
    }

    @Autowired
    PositionMapper positionMapper;

    @Autowired
    LocationMapper locationMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    BLocationMapper blocationMapper;

    @Autowired
    BCompanyMapper bcompanyMapper;

    @Autowired
    BPositionMapper bpositionMapper;
}
