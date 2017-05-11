package wuxian.me.lagouspider.save.lagou;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.mapper.lagou.LocationMapper;
import wuxian.me.lagouspider.model.lagou.Location;
import wuxian.me.lagouspider.save.IModelSaver;
import wuxian.me.lagouspider.save.SaveModelThread;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static wuxian.me.lagouspider.biz.lagou.LagouConfig.SaveDBThread.SAVE_LOCATION_INTERVAL;

/**
 * Created by wuxian on 17/4/2017.
 * <p>
 */
public class LocationSaver implements IModelSaver<Location> {

    private static LocationSaver instance = null;
    private Map<Long, Location> companyMap = new ConcurrentHashMap<Long, Location>();
    private SaveModelThread thread;

    private LocationMapper mapper = ModuleProvider.locationMapper();

    public static LocationSaver getInstance() {
        if (instance == null) {
            instance = new LocationSaver();
        }
        return instance;
    }


    private LocationSaver() {
        thread = new SaveModelThread(companyMap, SAVE_LOCATION_INTERVAL, new SaveModelThread.IDatabaseOperator<Location>() {
            public void insert(Location model) {
                mapper.insertLocation(model);
            }

            public void update(Location model) {
                //
            }
        });
        thread.setName("SaveLocationThread");
        thread.start();
    }

    public boolean saveModel(@NotNull Location location) {
        companyMap.put(location.index(), location);
        return true;
    }
}
