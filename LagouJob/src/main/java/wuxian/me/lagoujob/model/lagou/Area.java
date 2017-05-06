package wuxian.me.lagoujob.model.lagou;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by wuxian on 1/4/2017.
 */
public class Area {
    public int area_id = -1;
    public String name;
    public String distinct_name;

    @Override
    public int hashCode() {
        checkNotNull(name);
        checkNotNull(distinct_name);
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Area) {
            return distinct_name.equals(((Area) obj).distinct_name) && name.equals(((Area) obj).name);
        }

        return super.equals(obj);
    }

    public String name() {
        return "Area: {area_name: " + name + " ,distinct_name: " + distinct_name + "}";
    }

}
