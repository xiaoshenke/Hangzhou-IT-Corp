package wuxian.me.lagouspider.save.lagou;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.mapper.lagou.CompanyMapper;
import wuxian.me.lagouspider.model.lagou.Company;
import wuxian.me.lagouspider.save.IModelSaver;
import wuxian.me.lagouspider.save.SaveModelThread;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static wuxian.me.lagouspider.business.lagou.LagouConfig.SaveDBThread.SAVE_COMPANY_MAIN_INTERVAL;

/**
 * Created by wuxian on 8/4/2017.
 * <p>
 * 从拉勾的公司主页抓的数据
 */
public class CompanyMainSaver implements IModelSaver<Company> {

    private static CompanyMainSaver instance = null;

    private CompanyMapper mapper = ModuleProvider.companyMapper();

    public static CompanyMainSaver getInstance() {
        if (instance == null) {
            instance = new CompanyMainSaver();
        }
        return instance;
    }


    private CompanyMainSaver() {
        thread = new SaveModelThread(companyMap, SAVE_COMPANY_MAIN_INTERVAL, new SaveModelThread.IDatabaseOperator<Company>() {

            public void insert(Company model) {
                mapper.insertCompany(model);
            }

            public void update(Company model) {
                mapper.updateCompany(model);

            }
        }, false);
        thread.setName("SaveCompanyThreadMain");
        thread.start();
    }

    private Map<Long, Company> companyMap = new ConcurrentHashMap<Long, Company>();
    private SaveModelThread thread;


    public boolean saveModel(@NotNull Company company) {
        companyMap.put(company.company_id, company);
        return true;
    }
}
