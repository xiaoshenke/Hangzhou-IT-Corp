package wuxian.me.lagouspider.save.boss;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.biz.boss.BizConfig;
import wuxian.me.lagouspider.biz.boss.BossConfig;
import wuxian.me.lagouspider.mapper.boss.BCompanyMapper;
import wuxian.me.lagouspider.model.boss.BCompany;
import wuxian.me.lagouspider.save.BaseSaver;
import wuxian.me.lagouspider.save.IModelSaver;
import wuxian.me.lagouspider.save.SaveModelThread;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 17/4/2017.
 * <p>
 */
public class BCompanySaver extends BaseSaver<BCompany> {

    private static BCompanySaver instance = null;
    private Map<Long, BCompany> companyMap = new ConcurrentHashMap<Long, BCompany>();
    private SaveModelThread thread;

    private BCompanyMapper mapper = ModuleProvider.bCompanyMapper();

    public static BCompanySaver getInstance() {
        if (instance == null) {
            instance = new BCompanySaver();
        }
        return instance;
    }


    private BCompanySaver() {
        thread = new SaveModelThread(companyMap, BizConfig.saveCompanyInternal * 1000, new SaveModelThread.IDatabaseOperator<BCompany>() {
            public void insert(BCompany model) {
                mapper.insertCompany(model);
            }

            public void update(BCompany model) {
                //
            }
        });
        thread.setName("SaveBossCompanyThread");
        thread.start();
    }

    public boolean saveModel(@NotNull BCompany company) {
        if (!isModelValid(company)) {
            return false;
        }

        companyMap.put(company.companyId, company);
        return true;
    }

    //format一下model 使得数据可控
    public boolean isModelValid(@NotNull BCompany model) {

        if (model.name.length() > 8) {
            model.name = model.name.substring(0, 8);
        }

        if (model.fullName.length() > 16) {
            model.fullName = model.fullName.substring(0, 16);
        }

        if (model.field.length() > 8) {
            model.field = model.field.substring(0, 8);
        }

        if (model.logo.length() > 125) {
            model.logo = null;
        }

        return true;
    }


    @Override
    protected Thread getSaverThread() {
        return thread;
    }
}
