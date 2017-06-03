package wuxian.me.lagouspider.save.boss;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.biz.boss.BizConfig;
import wuxian.me.lagouspider.biz.boss.BossConfig;
import wuxian.me.lagouspider.mapper.boss.BPositionMapper;
import wuxian.me.lagouspider.model.boss.BPosition;
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
public class BPositionSaver extends BaseSaver<BPosition> {

    private static BPositionSaver instance = null;
    private Map<Long, BPosition> positionMap = new ConcurrentHashMap<Long, BPosition>();
    private SaveModelThread thread;

    private BPositionMapper mapper = ModuleProvider.bPositionMapper();

    public static BPositionSaver getInstance() {
        if (instance == null) {
            instance = new BPositionSaver();
        }
        return instance;
    }


    private BPositionSaver() {
        thread = new SaveModelThread(positionMap, BizConfig.savePositionInternal * 1000, new SaveModelThread.IDatabaseOperator<BPosition>() {
            public void insert(BPosition model) {
                mapper.insertPosition(model);
            }

            public void update(BPosition model) {
                //
            }
        });
        thread.setName("SaveBossPositionThread");
        thread.start();
    }

    public boolean saveModel(@NotNull BPosition position) {
        positionMap.put(position.positionId, position);
        return true;
    }

    public boolean isModelValid(@NotNull BPosition model) {
        return true;
    }

    @Override
    protected Thread getSaverThread() {
        return thread;
    }
}
