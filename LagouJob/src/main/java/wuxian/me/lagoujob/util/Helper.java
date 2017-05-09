package wuxian.me.lagoujob.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wuxian on 7/5/2017.
 */
public class Helper {

    private Helper() {
    }

    private static List fromCollection(Collection collection) {
        if (collection instanceof List) {
            return (List) collection;
        } else {
            return new ArrayList(collection);
        }
    }

    @Nullable
    public static <T> List filterAndReturn(@Nullable List<T> list, @NotNull Predicate<T> filter) {
        if (list == null || list.size() == 0) {
            return list;
        }

        Collection collection = Collections2.filter(list, filter);
        return fromCollection(collection);
    }
}
