package wuxian.me.lagoujob.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wuxian on 7/5/2017.
 */
public class Helper {

    private Helper() {
    }

    public static List fromCollection(Collection collection) {
        if (collection instanceof List) {
            return (List) collection;
        } else {
            return new ArrayList(collection);
        }
    }
}
