package wuxian.me.lagouspider.model;

import com.sun.istack.internal.NotNull;

/**
 * Created by wuxian on 30/3/2017.
 */
public class Distinct {

    private String distinct;

    public Distinct(@NotNull String distinct) {
        this.distinct = distinct;
    }

    public String getDistinct() {
        return distinct;
    }

    @Override
    public int hashCode() {
        //Fixme: log
        return distinct.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Distinct) {
            return distinct.equals(((Distinct) obj).distinct);
        }

        return super.equals(obj);
    }
}
