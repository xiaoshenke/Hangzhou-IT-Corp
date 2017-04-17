package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 8/4/2017.
 */
public class CompanyJsonSaver implements IModelSaver<Company> {

    private SaveModelThread thread;
    private CompanyMapper mapper = ModuleProvider.companyMapper();

    private CompanyJsonSaver() {
        thread = new SaveModelThread<Company>(companyMap, Config.SAVE_COMPANY_INTERVAL, new SaveModelThread.IDatabaseOperator<Company>() {

            public void insert(Company model) {
                mapper.insertCompany(model);
            }

            public void update(Company model) {
                mapper.updateCompany(model);

            }
        });
        thread.setName("save_company_json");
        thread.start();
    }

    private static CompanyJsonSaver instance = null;

    public static CompanyJsonSaver getInstance() {
        if (instance == null) {
            instance = new CompanyJsonSaver();
        }
        return instance;
    }

    private Map<Long, Company> companyMap = new ConcurrentHashMap<Long, Company>();


    public boolean saveModel(@NotNull Company company) {
        synchronized (companyMap) {
            if (company.detail_location != null && companyMap.keySet().contains(company.company_id)) {
                Company tmp = companyMap.get(company.company_id);
                tmp.detail_location = company.detail_location;

                tmp.webLink = company.webLink;
                tmp.logo = company.logo;
                tmp.lagouAuthentic = company.lagouAuthentic;
                tmp.description = company.description;
                tmp.financeStage = company.financeStage;
                tmp.positionNum = company.positionNum;
                tmp.resumeRate = company.resumeRate;
                tmp.interviewNum = company.interviewNum;
                tmp.score = company.score;
                tmp.accordSore = company.accordSore;
                tmp.interviewerScore = company.interviewerScore;
                tmp.environmentScore = company.environmentScore;

                tmp.detail_location = company.detail_location;

                logger().info("After merge: " + tmp.toString());
                companyMap.put(company.company_id, tmp);
                return true;
            }
        }

        companyMap.put(company.company_id, company);
        return false;
    }

}
