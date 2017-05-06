package wuxian.me.lagouspider.save.lagou;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.mapper.lagou.ProductMapper;
import wuxian.me.lagouspider.model.lagou.Product;
import wuxian.me.lagouspider.save.IModelSaver;
import wuxian.me.lagouspider.save.SaveModelThread;
import wuxian.me.lagouspider.util.ModuleProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static wuxian.me.lagouspider.business.lagou.LagouConfig.SaveDBThread.SAVE_PRODUCT_INTERVAL;

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
        thread = new SaveModelThread(companyMap, SAVE_PRODUCT_INTERVAL, new SaveModelThread.IDatabaseOperator<Product>() {

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
