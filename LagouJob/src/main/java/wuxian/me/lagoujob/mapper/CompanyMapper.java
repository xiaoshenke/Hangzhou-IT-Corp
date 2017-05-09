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

    List<Company> loadCompany(@Param("companyTable") String companyTable,
                              @Param("locationTable") String locationTable,
                              @Param("company_id") long company_id);

    List<Company> loadAll(@Param("companyTable") String companyTable,
                          @Param("locationTable") String locationTable);
}
