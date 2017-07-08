package wuxian.me.lagouspider.save.lagou;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.biz.boss.BizConfig;
import wuxian.me.lagouspider.mapper.boss.BPositionMapper;
import wuxian.me.lagouspider.mapper.lagou.PositionMapper;
import wuxian.me.lagouspider.model.boss.BPosition;
import wuxian.me.lagouspider.model.lagou.Position;
import wuxian.me.lagouspider.save.BaseSaver;
import wuxian.me.lagouspider.save.SaveModelThread;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 17/4/2017.
 * <p>
 */
public class PositionSaver extends BaseSaver<Position> {

    private static PositionSaver instance = null;
    private Map<Long, Position> positionMap = new ConcurrentHashMap<Long, Position>();
    private SaveModelThread thread;

    private PositionMapper mapper = ModuleProvider.positionMapper();

    public static PositionSaver getInstance() {
        if (instance == null) {
            instance = new PositionSaver();
        }
        return instance;
    }


    private PositionSaver() {
        thread = new SaveModelThread(positionMap, BizConfig.savePositionInternal * 1000, new SaveModelThread.IDatabaseOperator<Position>() {
            public void insert(Position model) {
                mapper.insertPosition(model);
            }

            public void update(Position model) {
                //
            }
        });
        thread.setName("SaveBossPositionThread");
        thread.start();
    }

    public boolean saveModel(@NotNull Position position) {
        positionMap.put(position.positionId, position);
        return true;
    }

    public boolean isModelValid(@NotNull Position model) {
        return true;
    }

    @Override
    protected Thread getSaverThread() {
        return thread;
    }
}
