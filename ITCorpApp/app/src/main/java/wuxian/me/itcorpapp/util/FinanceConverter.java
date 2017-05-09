package wuxian.me.itcorpapp.util;

import javax.xml.validation.Validator;

import wuxian.me.itcorpapp.model.Finance;

/**
 * Created by wuxian on 9/5/2017.
 * <p>
 * 融资state --> 描述转化工具类
 */

public class FinanceConverter {
    private FinanceConverter() {
    }

    public static String getDescriptionFrom(Finance finance) {
        String des = "Invalid";
        int val = finance.getValue();
        if (val == Finance.STAGE_TIANSHI.getValue()) {
            des = "天使";
        } else if (val == Finance.STAGE_A.getValue()) {
            des = "A轮";
        } else if (val == Finance.STAGE_B.getValue()) {
            des = "B轮";
        } else if (val == Finance.STAGE_C.getValue()) {
            des = "C轮";
        } else if (val == Finance.STAGE_D_OR_PLUS.getValue()) {
            des = "D或D+轮";
        } else if (val == Finance.STAGE_NONE.getValue()) {
            des = "未融资";
        } else if (val == Finance.STAGE_NONEED.getValue()) {
            des = "不需要融资";
        } else if (val == Finance.STAGE_SHANGSHI.getValue()) {
            des = "上市公司";
        }

        return des;
    }

    public static Finance fromStage(int stage) {

        if (stage < Finance.STAGE_TIANSHI.getValue() || stage > Finance.STAGE_SHANGSHI.getValue()) {
            return Finance.INVALID;
        }
        return Finance.values()[stage];
    }
}
