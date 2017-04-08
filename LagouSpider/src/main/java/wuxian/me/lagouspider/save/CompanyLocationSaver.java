package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.model.Company;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 8/4/2017.
 */
public class CompanyLocationSaver implements ICompanySaver {

    private static CompanyLocationSaver instance = null;

    public static CompanyLocationSaver getInstance() {
        if (instance == null) {
            instance = new CompanyLocationSaver();
        }
        return instance;
    }


    private CompanyLocationSaver() {
        thread = new SaveCompanyThread(companyMap, ICompanySaver.SAVE_COMPANY_LOCATION_INTERVAL, false);
        thread.setName("save_company_location");
        thread.start();
    }

    private Map<Long, Company> companyMap = new ConcurrentHashMap<Long, Company>();
    private SaveCompanyThread thread;


    public boolean saveCompany(@NotNull Company company) {
        companyMap.put(company.company_id, company);
        return true;
    }
}
