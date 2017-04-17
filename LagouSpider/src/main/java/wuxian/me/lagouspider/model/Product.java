package wuxian.me.lagouspider.model;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.core.DistinctSpider;

/**
 * Created by wuxian on 13/4/2017.
 * 产品来看 IT橙子的数据会更加靠谱
 * <p>
 */
public class Product extends BaseModel {
    public static String tableName;

    public long company_id;
    public String labelString = "";
    public String product_name;
    public String url;
    public String imgUrl;

    public String description;

    public Product(long company_id) {
        this.company_id = company_id;
    }

    public void addLabel(@NotNull String label) {
        labelString += label + DistinctSpider.CUT;
    }

    @Override
    public int hashCode() {
        return product_name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            return ((Product) obj).company_id == company_id &&
                    ((Product) obj).product_name.equals(product_name);
        }

        return super.equals(obj);
    }

    public String name() {
        return "Product: {company_id: " + company_id + " product_name: " + product_name + " url: " + url + " labelstring: " + labelString + "}";
    }

}
