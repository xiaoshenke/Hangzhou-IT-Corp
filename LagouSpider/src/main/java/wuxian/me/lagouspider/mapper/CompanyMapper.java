package wuxian.me.lagouspider.mapper;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.model.Area;
import wuxian.me.lagouspider.model.Company;

import java.util.List;

/**
 * Created by wuxian on 6/4/2017.
 * <p>
 * tableName: companies_2017_03_03,companies_2017_03_10,etc
 */
public interface CompanyMapper {

    void createNewTableIfNeed(@Param("tableName") String tableName);

    void insertCompany(@Param("tableName") String tableName,
                       @Param("company_id") long companyId,
                       @Param("areaId") int areaId,
                       @Param("fullName") String fullName,
                       @Param("stage") String stage,
                       @Param("field") String field);

    void updateCompanyLocation(@Param("tableName") String tableName,
                               @Param("company_id") long companyId,
                               @Param("location") String location);

    List<Company> loadAllCompanies(@Param("tableName") String tableName);
}