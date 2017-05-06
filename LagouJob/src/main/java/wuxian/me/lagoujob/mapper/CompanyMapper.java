package wuxian.me.lagoujob.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import wuxian.me.lagoujob.mapper.BaseMapper;
import wuxian.me.lagoujob.model.lagou.Company;

import java.util.List;

/**
 * Created by wuxian on 6/4/2017.
 */
public interface CompanyMapper {

    Company loadCompany(@Param("tableName") String tableName, @Param("company_id") long company_id);

    List<Company> loadAllCompanies(@Param("tableName") String tableName);
}
