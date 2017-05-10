package wuxian.me.itcorpapp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by wuxian on 9/5/2017.
 */

@Entity
public class Location {

    @Index
    @Property(nameInDb = "company_id")
    public long company_id;

    @Id(autoincrement = true)
    @Property(nameInDb = "location_id")
    public long locationId;

    @Property(nameInDb = "location_des")
    public String location;

    @Property(nameInDb = "longitude")
    public String longitude;

    @Property(nameInDb = "lantitude")
    public String lantitude;

    public Location() {

    }

    @Generated(hash = 544772635)
    public Location(long company_id, long locationId, String location,
                    String longitude, String lantitude) {
        this.company_id = company_id;
        this.locationId = locationId;
        this.location = location;
        this.longitude = longitude;
        this.lantitude = lantitude;
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
        return "Location: {" +
                "location: " + location + " longitude: " +
                longitude + " lantitude: " + lantitude + "}";
    }

    public long getCompany_id() {
        return this.company_id;
    }

    public void setCompany_id(long company_id) {
        this.company_id = company_id;
    }

    public long getLocationId() {
        return this.locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLantitude() {
        return this.lantitude;
    }

    public void setLantitude(String lantitude) {
        this.lantitude = lantitude;
    }

}