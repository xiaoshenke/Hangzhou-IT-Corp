package wuxian.me.lagouspider.model.boss;

import wuxian.me.lagouspider.biz.boss.BossConfig;
import wuxian.me.lagouspider.model.BaseModel;

/**
 * Created by wuxian on 17/4/2017.
 */
public class BLocation extends BaseModel {

    public static String tableName = BossConfig.TableName.LOCATION;

    public long locationId = -1;
    public long companyId = -1;

    public String location;

    public String longitude;
    public String lantitude;

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
        return locationId != -1 ? locationId :
                companyId + hashCode();
    }
}
