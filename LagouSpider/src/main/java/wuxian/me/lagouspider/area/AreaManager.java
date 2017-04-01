package wuxian.me.lagouspider.area;

import wuxian.me.lagouspider.model.Area;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuxian on 1/4/2017.
 */
public class AreaManager {

    private AreaManager() {
    }

    private static AreaManager manager;

    private Map<Area, Integer> companyNumMap = new HashMap<Area, Integer>();

    public static AreaManager getInstance() {
        if (manager == null) {
            manager = new AreaManager();
        }
        return manager;
    }

    public synchronized boolean contains(Area area) {

        return true;
    }
}
