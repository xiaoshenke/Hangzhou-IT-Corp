package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.model.Company;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 8/4/2017.
 *
 * 从拉勾的公司主页抓的数据
 */
public class CompanyMainSaver implements ICompanySaver {

    private static CompanyMainSaver instance = null;

    public static CompanyMainSaver getInstance() {
        if (instance == null) {
            instance = new CompanyMainSaver();
        }
        return instance;
    }


    private CompanyMainSaver() {
        thread = new SaveCompanyThread(companyMap, ICompanySaver.SAVE_COMPANY_MAIN_INTERVAL, false);
        thread.setName("save_company_main");
        thread.start();
    }

    private Map<Long, Company> companyMap = new ConcurrentHashMap<Long, Company>();
    private SaveCompanyThread thread;


    public boolean saveCompany(@NotNull Company company) {
        companyMap.put(company.company_id, company);
        return true;
    }
}
