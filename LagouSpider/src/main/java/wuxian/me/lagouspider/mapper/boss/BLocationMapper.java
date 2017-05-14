package wuxian.me.lagouspider.mapper.boss;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.mapper.BaseMapper;
import wuxian.me.lagouspider.model.boss.BLocation;

import java.util.List;

/**
 * Created by wuxian on 13/4/2017.
 */
public interface BLocationMapper extends BaseMapper<BLocation> {

    void deleteTable(BLocation location);

    void insertLocation(BLocation location);

    void updateLocation(BLocation location);

    List<BLocation> loadLocation(@Param("tableName") String tableName, long company_id);
}
