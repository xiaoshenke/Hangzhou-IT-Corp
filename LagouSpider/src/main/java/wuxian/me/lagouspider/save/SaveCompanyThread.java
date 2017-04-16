package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.util.Helper;
import wuxian.me.lagouspider.util.ModuleProvider;
import java.util.HashMap;
import java.util.Map;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

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

    private CompanyMapper mapper = ModuleProvider.companyMapper();

    @Override
    public void run() {
        while (true) {
            if (!companies.isEmpty()) {
                Map<Long, Company> companyMap;

                synchronized (companies) {
                    companyMap = new HashMap<Long, Company>(companies);
                    companies.clear();
                }

                for (Company company : companyMap.values()) {
                    if (insert) {
                        logger().info("SaveCompanyThread: Insert");
                        mapper.insertCompany(company);

                    } else {
                        logger().info("SaveCompanyThread: Update");
                        mapper.updateCompany(company);

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
