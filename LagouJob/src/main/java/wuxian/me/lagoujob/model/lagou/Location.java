package wuxian.me.lagoujob.model.lagou;

import com.google.gson.annotations.Expose;
import wuxian.me.lagoujob.model.BaseModel;

/**
 * Created by wuxian on 17/4/2017.
 */
public class Location extends BaseModel {
    public static String tableName;

    public String location;

    public long company_id;

    public String longitude;
    public String lantitude;

    @Expose(serialize = false)
    public long locationId;

    public Location() {

    }

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

    public String name() {
        return "Location: {" + "company_id: " + company_id +
                " location: " + location + " longitude: " +
                longitude + " lantitude: " + lantitude + "}";
    }

    public long index() {
        return company_id + hashCode();
    }
}
