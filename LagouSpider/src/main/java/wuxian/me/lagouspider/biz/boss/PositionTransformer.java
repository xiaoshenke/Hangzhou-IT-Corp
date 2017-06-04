package wuxian.me.lagouspider.biz.boss;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 13/5/2017.
 */
public class PositionTransformer {

    private static List<String> positionList;
    private static List<String> positionCodeList;

    static {

        positionList = new ArrayList<String>();
        positionCodeList = new ArrayList<String>();

        positionCodeList.add("100101");
        positionList.add("java");

        positionCodeList.add("100202");
        positionList.add("android");

        positionCodeList.add("100102");
        positionList.add("c++");

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

    public static String getPosition(String code) {
        int index = getIndex(positionCodeList, code);

        return index == -1 ? null : positionList.get(index);
    }

    public static String getPositionCode(String city) {
        int index = getIndex(positionList, city);

        return index == -1 ? null : positionCodeList.get(index);
    }

    private PositionTransformer() {
    }
}
