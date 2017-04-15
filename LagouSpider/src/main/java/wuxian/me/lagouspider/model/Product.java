package wuxian.me.lagouspider.model;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 13/4/2017.
 * 产品来看 IT橙子的数据会更加靠谱
 * Todo: 标签优化 ？？？？
 */
public class Product {

    public long company_id;

    public static int TYPE_OTHER = 0;
    public static int TYPE_WEB = 1;
    public static int TYPE_MOBILE = 1 << 1;  //这个type似乎不应该以int为类型

    public List<String> typeList = new ArrayList<String>();

    public void addType(@NotNull String type) {
        typeList.add(type);
    }

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
