package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.mapper.LocationMapper;
import wuxian.me.lagouspider.model.Location;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        thread = new SaveModelThread(companyMap, Config.SAVE_LOCATION_INTERVAL, new SaveModelThread.IDatabaseOperator<Location>() {
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
        companyMap.put(location.company_id, location);
        return true;
    }
}
