package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.model.Company;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wuxian on 8/4/2017.
 */
public class CompanyJsonSaver implements ICompanySaver {

    private SaveCompanyThread thread;

    private CompanyJsonSaver() {
        thread = new SaveCompanyThread(companyMap, ICompanySaver.SAVE_COMPANY_INTERVAL, true);
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

    public boolean saveCompany(@NotNull Company company) {
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
                companyMap.put(company.company_id, tmp);
                return true;
            }
        }

        companyMap.put(company.company_id, company);
        return false;
    }

}
