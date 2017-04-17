package wuxian.me.lagouspider.model;

/**
 * Created by wuxian on 17/4/2017.
 */
public class Location {
    public static String tableName;

    public String location;
    public long company_id;

    public Location(long company_id, String location) {
        this.company_id = company_id;
        this.location = location;
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
