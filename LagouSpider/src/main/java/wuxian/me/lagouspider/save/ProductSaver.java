package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.Config;
import wuxian.me.lagouspider.mapper.ProductMapper;
import wuxian.me.lagouspider.model.Product;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wuxian on 17/4/2017.
 * <p>
 */
public class ProductSaver implements IModelSaver<Product> {
    private static ProductSaver instance = null;

    private ProductMapper mapper = ModuleProvider.productMapper();

    public static ProductSaver getInstance() {
        if (instance == null) {
            instance = new ProductSaver();
        }
        return instance;
    }


    private ProductSaver() {
        thread = new SaveModelThread(companyMap, Config.SAVE_PRODUCT_INTERVAL, new SaveModelThread.IDatabaseOperator<Product>() {

            public void insert(Product model) {
                mapper.insertProduct(model);
            }

            public void update(Product model) {
                //
            }
        });
        thread.setName("SaveProductThread");
        thread.start();
    }

    private Map<Long, Product> companyMap = new ConcurrentHashMap<Long, Product>();
    private SaveModelThread thread;

    public boolean saveModel(@NotNull Product product) {
        companyMap.put(product.index(), product);
        return true;
    }
}
