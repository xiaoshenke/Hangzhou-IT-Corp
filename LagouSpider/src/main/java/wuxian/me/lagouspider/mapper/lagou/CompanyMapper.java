package wuxian.me.lagouspider.mapper.lagou;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.mapper.BaseMapper;
import wuxian.me.lagouspider.model.lagou.Company;

import java.util.List;

/**
 * Created by wuxian on 6/4/2017.
 * <p>
 * tableName: companies_2017_03_03,companies_2017_03_10,etc
 *
 * 这里可以重构一下 重构出来一个BaseMapper
 */
public interface CompanyMapper extends BaseMapper<Company> {

    void deleteTable(Company company);

    void insertCompany(Company company);

    void updateCompany(Company company);

    List<Company> loadAllCompanies(@Param("tableName") String tableName);
}
