package wuxian.me.lagouspider.model.boss;

import wuxian.me.lagouspider.model.BaseModel;

/**
 * Created by wuxian on 13/5/2017.
 */
public class BCompany extends BaseModel {

    //Todo: logo存起来有意义么？
    //public String logo;

    public String financeStage;

    public String sizeMin;
    public String sizeMax;

    public String field;

    public String name;

    public String fullName;

    public long companyId;

    public String name() {
        return "BossCompany: name:" + name + " fullName:" + fullName + " companyId:" + companyId
                + " finaceStage:" + financeStage + " field:" + field + " sizeMin:" + sizeMin + " sizeMax:" + sizeMax;
    }
}
