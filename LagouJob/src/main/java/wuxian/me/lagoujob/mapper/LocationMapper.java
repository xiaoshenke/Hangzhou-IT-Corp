package wuxian.me.lagoujob.mapper;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagoujob.model.lagou.Location;

import java.util.List;

/**
 * Created by wuxian on 13/4/2017.
 */
public interface LocationMapper {

    void updateLocation(Location location);

    List<Location> loadLocation(@Param("tableName") String tableName, @Param("company_id") long company_id);

    List<Location> loadAll(@Param("tableName") String tableName);
}
