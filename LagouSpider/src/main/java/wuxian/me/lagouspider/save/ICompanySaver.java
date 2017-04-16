package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.model.Company;

/**
 * Created by wuxian on 8/4/2017.
 */
public interface ICompanySaver {

    String LOCATION_MULTY = "multi";
    String LOCATION_NONE = "none";

    //每5分钟存储一次数据库
    int SAVE_COMPANY_INTERVAL = 60 * 1000 * 1;
    int SAVE_COMPANY_MAIN_INTERVAL = (int) (60 * 1000 * 1.5);

    /**
     * @return datachanged
     */
    boolean saveCompany(@NotNull Company company);
}
