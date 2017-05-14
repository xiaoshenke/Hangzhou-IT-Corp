package wuxian.me.lagouspider.mapper.boss;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.mapper.BaseMapper;
import wuxian.me.lagouspider.model.boss.BCompany;

import java.util.List;

/**
 * Created by wuxian on 6/4/2017.
 */
public interface BCompanyMapper extends BaseMapper<BCompany> {

    void deleteTable(BCompany company);

    void insertCompany(BCompany company);

    List<BCompany> loadAll(@Param("tableName") String tableName);
}
