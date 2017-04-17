package wuxian.me.lagouspider.mapper;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.model.Area;

import java.util.List;

/**
 * Created by wuxian on 1/4/2017.
 */
public interface AreaMapper {
    Area getArea(@Param("product_name") String name);

    void insertArea(@Param("areaName") String areaName, @Param("distinctName") String distinctName);

    List<Area> loadAreaOfDistinct(@Param("distinctName") String distinctName);

    List<Area> loadAll();
}
