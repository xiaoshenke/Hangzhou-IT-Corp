package wuxian.me.lagouspider.model.boss;

import wuxian.me.lagouspider.model.BaseModel;

/**
 * Created by wuxian on 17/4/2017.
 */
public class BLocation extends BaseModel {

    //通过currentmill和location的hashString计算而来 确保唯一性
    public long locationId;
    public String location;
    public long companyId;
    public String longitude;
    public String latitude;

    public BLocation() {
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
        return "Location: {" + "company_id: " + companyId + " location: " + location + "}";
    }

    public long index() {
        return companyId + hashCode();
    }
}
