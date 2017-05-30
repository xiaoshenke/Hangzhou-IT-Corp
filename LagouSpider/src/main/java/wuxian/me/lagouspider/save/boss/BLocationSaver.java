package wuxian.me.lagouspider.save.boss;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.biz.boss.BizConfig;
import wuxian.me.lagouspider.biz.boss.BossConfig;
import wuxian.me.lagouspider.mapper.boss.BLocationMapper;
import wuxian.me.lagouspider.model.boss.BLocation;
import wuxian.me.lagouspider.save.IModelSaver;
import wuxian.me.lagouspider.save.SaveModelThread;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 17/4/2017.
 * <p>
 */
public class BLocationSaver implements IModelSaver<BLocation> {

    private static BLocationSaver instance = null;
    private Map<Long, BLocation> companyMap = new ConcurrentHashMap<Long, BLocation>();
    private SaveModelThread thread;

    private BLocationMapper mapper = ModuleProvider.bLocationMapper();

    public static BLocationSaver getInstance() {
        if (instance == null) {
            instance = new BLocationSaver();
        }
        return instance;
    }


    private BLocationSaver() {
        thread = new SaveModelThread(companyMap, BizConfig.saveLocationInternal * 1000, new SaveModelThread.IDatabaseOperator<BLocation>() {
            public void insert(BLocation model) {
                mapper.insertLocation(model);
            }

            public void update(BLocation model) {
                //
            }
        });
        thread.setName("SaveBossLocationThread");
        thread.start();
    }

    public boolean saveModel(@NotNull BLocation location) {
        if (!isModelValid(location)) {
            return false;
        }
        companyMap.put(location.index(), location);
        return true;
    }

    public boolean isModelValid(@NotNull BLocation model) {

        if (model.locationId == -1) {
            return false;
        }
        return true;
    }
}
