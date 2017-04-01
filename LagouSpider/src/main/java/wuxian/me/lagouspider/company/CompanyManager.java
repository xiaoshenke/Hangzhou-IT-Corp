package wuxian.me.lagouspider.company;

import wuxian.me.lagouspider.model.Company;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuxian on 1/4/2017.
 */
public class CompanyManager {
    private CompanyManager() {
    }

    private static CompanyManager manager;

    private Map<Company, Integer> jobNumMap = new HashMap<Company, Integer>();

    public static CompanyManager getInstance() {
        if (manager == null) {
            manager = new CompanyManager();
        }
        return manager;
    }

    public synchronized boolean contains(Company area) {

        return true;
    }
}
