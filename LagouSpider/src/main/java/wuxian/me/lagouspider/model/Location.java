package wuxian.me.lagouspider.model;

/**
 * Created by wuxian on 17/4/2017.
 */
public class Location {
    public static String tableName;

    public String location;
    public long company_id;

    public Location(long company_id){
        this.company_id = company_id;
    }
}
