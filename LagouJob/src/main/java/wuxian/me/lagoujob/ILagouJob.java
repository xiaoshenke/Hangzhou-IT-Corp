package wuxian.me.lagoujob;

import wuxian.me.lagoujob.model.lagou.Company;

import java.util.List;

/**
 * Created by wuxian on 1/5/2017.
 * <p>
 * 这里列出一些应该提供的api
 */
public interface ILagouJob {

    //7天抓一次 因此会有n个列表
    List<Long> listTables();

    //杭州所有区的数据
    List<Company> loadAllCompanies(long date);

    //没有date默认为最近一次的全数据
    List<Company> loadAllCompanies();

}
