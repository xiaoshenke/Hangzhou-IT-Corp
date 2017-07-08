package wuxian.me.lagouspider.mapper.lagou;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.mapper.BaseMapper;
import wuxian.me.lagouspider.model.boss.BPosition;
import wuxian.me.lagouspider.model.lagou.Position;

import java.util.List;

public interface PositionMapper extends BaseMapper<Position> {

    void deleteTable(Position position);

    void insertPosition(Position position);

    List<BPosition> loadAll(@Param("tableName") String tableName);
}