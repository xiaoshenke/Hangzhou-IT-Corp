package wuxian.me.lagouspider.model.boss;

import wuxian.me.lagouspider.biz.boss.BossConfig;
import wuxian.me.lagouspider.model.BaseModel;

/**
 * Created by wuxian on 13/5/2017.
 */
public class BCompany extends BaseModel {

    public static String tableName = BossConfig.TableName.COMPANY;

    //做个校验：最长125字节
    public String logo;

    public String financeStage;

    public int sizeMin;
    public int sizeMax;

    public String field;

    //max:8 长过这个的截取
    public String name;

    //max:16
    public String fullName;

    public long companyId = -1;

    public String name() {
        return "BossCompany: name:" + name + " fullName:" + fullName + " companyId:" + companyId
                + " finaceStage:" + financeStage + " field:" + field + " sizeMin:" + sizeMin + " sizeMax:" + sizeMax;
    }
}
