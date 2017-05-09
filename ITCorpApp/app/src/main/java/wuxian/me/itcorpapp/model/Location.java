package wuxian.me.itcorpapp.model;

/**
 * Created by wuxian on 9/5/2017.
 */

public class Location {

    public String location;
    public String longitude;
    public String lantitude;

    public Location() {

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

}