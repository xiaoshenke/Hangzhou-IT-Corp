package wuxian.me.lagouspider.mapper;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.model.Location;

import java.util.List;

/**
 * Created by wuxian on 13/4/2017.
 */
public interface LocationMapper {

    void deleteTable(Location location);

    void createNewTableIfNeed(Location location);

    void createIndex(Location location);

    void insertLocation(Location location);

    void updateLocation(Location location);

    List<Location> loadLocation(@Param("tableName") String tableName, long company_id);
}
