package wuxian.me.lagouspider.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wuxian.me.lagouspider.Main;
import wuxian.me.lagouspider.mapper.AreaMapper;
import wuxian.me.lagouspider.mapper.CompanyMapper;

import static wuxian.me.lagouspider.util.FileUtil.getLog4jPropFilePath;

/**
 * Created by wuxian on 8/4/2017.
 */
@Component
public class ModuleProvider {

    private static Logger logger = Logger.getLogger(Main.class);
    private static ApplicationContext ctx;
    private static ModuleProvider instance;

    static {
        ctx = new ClassPathXmlApplicationContext("spider.xml");
        instance = ctx.getBean(ModuleProvider.class);
        PropertyConfigurator.configure(getLog4jPropFilePath());
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

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    CompanyMapper companyMapper;
}
