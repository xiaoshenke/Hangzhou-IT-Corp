package wuxian.me.lagouspider.model.boss;

import wuxian.me.lagouspider.biz.boss.BossConfig;
import wuxian.me.lagouspider.model.BaseModel;

/**
 * Created by wuxian on 13/5/2017.
 */
public class BPosition extends BaseModel {

    public static String tableName = BossConfig.TableName.POSITION;

    public long positionId;

    public String positionName;

    public int salaryMin = -1;

    public int salaryMax = -1;

    public String city;

    public long companyId;

    public String description;

    public int experienceMin = -1;

    public int experienceMax = -1;

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
