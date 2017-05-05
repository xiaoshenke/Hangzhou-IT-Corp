package wuxian.me.lagouspider.mapper;

import org.apache.ibatis.annotations.Param;
import wuxian.me.lagouspider.model.Product;

import java.util.List;

/**
 * Created by wuxian on 13/4/2017.
 * <p>
 */
public interface ProductMapper extends BaseMapper<Product> {

    void deleteTable(Product product);

    void insertProduct(Product product);

    List<Product> loadProduct(@Param("tableName") String tableName, long company_id);

}
