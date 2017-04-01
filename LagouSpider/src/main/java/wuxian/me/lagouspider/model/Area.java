package wuxian.me.lagouspider.model;

/**
 * Created by wuxian on 30/3/2017.
 */
public class Area {

    private Distinct distinct;
    private String area;

    public Area(Distinct distinct, String area) {
        this.distinct = distinct;
        this.area = area;
    }

    //Todo
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    //Todo
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
