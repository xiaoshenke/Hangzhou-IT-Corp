package wuxian.me.lagouspider.mapper.boss;


import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.mapper.BaseMapper;
import wuxian.me.lagouspider.model.boss.BPosition;

import java.util.List;

/**
 * Created by wuxian on 6/4/2017.
 */
public interface BPositionMapper extends BaseMapper<BPosition> {

    void deleteTable(BPosition position);

    void insertPosition(BPosition position);

    List<BPosition> loadAll(@Param("tableName") String tableName);
}
