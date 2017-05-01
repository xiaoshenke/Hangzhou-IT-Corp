package wuxian.me.lagoujob;

import wuxian.me.lagoujob.model.Company;

import java.util.List;

/**
 * Created by wuxian on 1/5/2017.
 * <p>
 * 这里列出一些应该提供的api
 */
public interface ILagouJob {

    //7天抓一次 因此会有n个列表
    List<Long> listDatesOfData();

    //杭州所有区的数据
    List<Company> loadAllCompanies(long date);

    //没有date默认为最近一次的全数据
    List<Company> loadAllCompanies();

    //Todo:根据地区来,根据街道来
    List<Company> loadCompanies();

    //Todo:
    List<Company> searchCompany();

    //Todo: 提供类似统计分析之类的api？API设计？

}
