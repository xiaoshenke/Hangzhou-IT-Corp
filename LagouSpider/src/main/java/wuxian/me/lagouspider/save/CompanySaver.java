package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.model.Company;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 8/4/2017.
 * 一个company会被两次赋值：
 * 一次是抓岗位列表页的时候被初次赋值:对应的数据库操作是insert
 * 第二次是通过岗位id拿到公司的具体地址:对应的数据库操作是update
 * 因此这里设计一个缓冲机制
 *
 */
public class CompanySaver implements IModelSaver<Company> {

    private static CompanySaver instance = null;

    public static CompanySaver getInstance() {
        if (instance == null) {
            instance = new CompanySaver();
        }
        return instance;
    }

    private CompanyJsonSaver jsonSaver = CompanyJsonSaver.getInstance();
    private CompanyMainSaver locationSaver = CompanyMainSaver.getInstance();

    private CompanySaver() {
    }

    public boolean saveModel(@NotNull Company company) {
        logger().info("CompanySaver.saveModel company: " + company.toString());
        if (company.detail_location != null) {  //这是一个location类型的company 先看看json类型里有没有
            if (jsonSaver.saveModel(company)) {
                logger().info("jsonSaver return true");
                return true;
            }
            logger().info("jsonSaver return false");
            locationSaver.saveModel(company);
            return true;
        }
        jsonSaver.saveModel(company);
        return true;
    }
}
