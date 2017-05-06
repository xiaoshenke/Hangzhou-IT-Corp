package wuxian.me.lagoujob.mapper.lagou;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagoujob.model.lagou.Location;

import java.util.List;

/**
 * Created by wuxian on 13/4/2017.
 */
public interface LocationMapper {

    void updateLocation(Location location);

    List<Location> loadLocation(Location location);

    List<Location> loadAll(@Param("tableName") String tableName);
}
