package wuxian.me.lagouspider.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import wuxian.me.lagouspider.Main;
import wuxian.me.lagouspider.mapper.boss.BCompanyMapper;
import wuxian.me.lagouspider.mapper.boss.BLocationMapper;
import wuxian.me.lagouspider.mapper.boss.BPositionMapper;
import wuxian.me.lagouspider.mapper.lagou.AreaMapper;
import wuxian.me.lagouspider.mapper.lagou.CompanyMapper;
import wuxian.me.lagouspider.mapper.lagou.LocationMapper;
import wuxian.me.lagouspider.mapper.lagou.ProductMapper;

import static wuxian.me.lagouspider.util.Helper.getLog4jPropFilePath;

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
