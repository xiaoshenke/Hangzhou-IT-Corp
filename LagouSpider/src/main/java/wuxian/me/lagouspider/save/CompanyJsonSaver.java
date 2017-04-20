package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.mapper.CompanyMapper;
import wuxian.me.lagouspider.model.Company;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static wuxian.me.lagouspider.Config.SaveDBThread.SAVE_COMPANY_INTERVAL;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 8/4/2017.
 */
public class CompanyJsonSaver implements IModelSaver<Company> {

    private SaveModelThread thread;
    private CompanyMapper mapper = ModuleProvider.companyMapper();

    private CompanyJsonSaver() {
        thread = new SaveModelThread<Company>(companyMap, SAVE_COMPANY_INTERVAL, new SaveModelThread.IDatabaseOperator<Company>() {

            public void insert(Company model) {
                mapper.insertCompany(model);
            }

            public void update(Company model) {
                mapper.updateCompany(model);

            }
        });
        thread.setName("SaveCompanyThreadJson");
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

    private Set<Long> savedComany = new HashSet<Long>();  //解决插入company重复问题

    public boolean saveModel(@NotNull Company company) {
        synchronized (companyMap) {

            if (company.detail_location == null) {
                if (savedComany.contains(company.company_id)) {  //重复数据 直接把这个数据丢了
                    return true;
                } else {
                    companyMap.put(company.company_id, company); //放入待插入数据队列
                    savedComany.add(company.company_id);
                    return true;
                }

            } else {             //这是从company main抓的数据,若要放入待插入数据除非满足本身已经在被插入队列
                if (!companyMap.keySet().contains(company.company_id)) {
                    return false;
                }
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

                savedComany.add(company.company_id);
                return true;
            }
        }
    }

}
