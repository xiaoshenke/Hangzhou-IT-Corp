package wuxian.me.lagouspider.model;

import com.sun.istack.internal.NotNull;

/**
 * Created by wuxian on 30/3/2017.
 */
public class Area {

    private Distinct distinct;
    private String area;

    public Area(@NotNull Distinct distinct, @NotNull String area) {
        this.distinct = distinct;
        this.area = area;
    }


    @Override
    public int hashCode() {
        return area.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Area) {
            return distinct.equals(((Area) obj).distinct) && area.equals(((Area) obj).area);
        }
        return super.equals(obj);
    }
}
