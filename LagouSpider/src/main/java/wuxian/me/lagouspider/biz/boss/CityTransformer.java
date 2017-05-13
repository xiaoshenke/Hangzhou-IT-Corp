package wuxian.me.lagouspider.biz.boss;

import com.sun.istack.internal.NotNull;

import java.util.*;

/**
 * Created by wuxian on 13/5/2017.
 */
public class CityTransformer {

    private static List<String> cityList;
    private static List<String> cityCodeList;

    static {

        cityList = new ArrayList<String>();
        cityCodeList = new ArrayList<String>();

        cityCodeList.add("101010100");
        cityList.add("北京");

        cityCodeList.add("101210100");
        cityList.add("杭州");

        cityCodeList.add("101020100");
        cityList.add("上海");

        cityCodeList.add("101280100");
        cityList.add("广州");

        cityCodeList.add("101280600");
        cityList.add("深圳");
    }

    /**
     * return -1表示没有找到
     */
    private static int getIndex(@NotNull List<String> list, @NotNull String key) {

        int ret = -1;
        if (list.size() == 0) {
            return ret;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(key)) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    public static String getCity(String code) {
        int index = getIndex(cityCodeList, code);

        return index == -1 ? null : cityList.get(index);
    }

    public static String getCityCode(String city) {
        int index = getIndex(cityList, city);

        return index == -1 ? null : cityCodeList.get(index);
    }

    private CityTransformer() {
    }
}
