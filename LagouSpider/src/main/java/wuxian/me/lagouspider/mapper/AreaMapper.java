package wuxian.me.lagouspider.mapper;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.model.Area1;

/**
 * Created by wuxian on 1/4/2017.
 */
public interface AreaMapper {
    Area1 getArea(@Param("name") String name);

    void insertArea(@Param("areaName") String areaName, @Param("distinctName") String distinctName);
}
