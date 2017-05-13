package wuxian.me.lagouspider.model.boss;

import wuxian.me.lagouspider.model.BaseModel;

/**
 * Created by wuxian on 13/5/2017.
 */
public class BPosition extends BaseModel {

    public long positionId;

    public String positionName;

    public String salaryMin = "-1";

    public String salaryMax = "-1";

    public String city;

    public long companyId;

    public String description;

    public String experienceMin = "-1";

    public String experienceMax = "-1";

    public String education;

    //发布时间
    public String postTime;

    public long locationId;

    public BPosition() {
        ;
    }

    public String name() {
        return "BossPosition: " + positionId;
    }
}
