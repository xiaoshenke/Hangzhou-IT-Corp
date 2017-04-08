package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuxian on 8/4/2017.
 */
public class SaveCompanyThread extends Thread {

    private Map<Long, Company> companies;
    private int interval;
    private boolean insert;

    public SaveCompanyThread(@NotNull Map<Long, Company> companies, int interval, boolean insert) {
        this.companies = companies;
        this.interval = interval;
        this.insert = insert;
    }

    private CompanyMapper mapper = ModuleProvider.getInstance().companyMapper;
    private String tableName = Helper.getCompanyTableName();

    @Override
    public void run() {
        while (true) {
            if (!companies.isEmpty()) {
                Map<Long, Company> companyMap = new HashMap<Long, Company>(companies);
                companies.clear();

                for (Company company : companyMap.values()) {

                    if (insert) {
                        mapper.insertCompany(tableName, company.company_id,
                                company.area_id, company.company_fullname,
                                company.financeStage, company.industryField);
                    } else {
                        ;//Todo: update
                    }
                }
            }

            try {
                sleep(interval);
            } catch (InterruptedException e) {
                ;
            }
        }

    }
}
