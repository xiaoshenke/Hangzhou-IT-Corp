package wuxian.me.lagouspider.model;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.core.DistinctSpider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 13/4/2017.
 * 产品来看 IT橙子的数据会更加靠谱
 */
public class Product {

    public long company_id;

    public List<String> labelList = new ArrayList<String>();

    public void addLabel(@NotNull String label) {
        labelList.add(label);

        //labelString = new Gson().toJson(labelList);
        labelString += label + DistinctSpider.CUT;
    }

    public String labelString = "";

    public String product_name;
    public String url;
    public String imgUrl;

    public String description;

    public Product(long company_id) {
        this.company_id = company_id;
    }

}
