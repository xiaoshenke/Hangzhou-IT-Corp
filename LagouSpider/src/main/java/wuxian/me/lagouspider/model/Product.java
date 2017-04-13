package wuxian.me.lagouspider.model;

/**
 * Created by wuxian on 13/4/2017.
 */
public class Product {

    public long company_id;

    public static int TYPE_OTHER = 0;
    public static int TYPE_WEB = 1;
    public static int TYPE_MOBILE = 1 << 1;

    public int type;
    public String name;
    public String url;
    public String imgUrl;

    public String description;

    public Product(long company_id) {
        this.company_id = company_id;
    }

    public boolean isWebApplication() {
        return (type & TYPE_WEB) != 0;
    }

    public boolean isMobileApp() {
        return (type & TYPE_MOBILE) != 0;
    }

    public boolean isOther() {
        return type == TYPE_OTHER;
    }

}
