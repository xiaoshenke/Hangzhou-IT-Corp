package wuxian.me.lagoujob.util;

import com.google.common.base.Predicate;
import org.springframework.util.StringUtils;
import wuxian.me.lagoujob.model.lagou.Location;

/**
 * Created by wuxian on 9/5/2017.
 */
public class GeoFilter implements Predicate<Location> {

    public boolean test(Location input) {
        return apply(input);
    }

    public boolean apply(Location location) {
        if (location != null) {
            if (valid(location.lantitude) && valid(location.longitude)) {
                return true;
            }
        }

        return false;
    }

    private boolean valid(String geo) {
        return !StringUtils.isEmpty(geo) && !(geo.equals("-1"));
    }
}
