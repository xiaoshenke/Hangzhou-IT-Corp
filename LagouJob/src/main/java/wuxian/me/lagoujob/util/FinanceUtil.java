package wuxian.me.lagoujob.util;

import wuxian.me.lagoujob.model.lagou.Company;

/**
 * Created by wuxian on 7/5/2017.
 */
public class FinanceUtil {

    private FinanceUtil() {
    }

    public static Finance from(Company company) {
        String finance = company.financeStage;
        if (finance.contains("天使")) {
            return Finance.STAGE_TIANSHI;
        } else if (finance.contains("A")) {
            return Finance.STAGE_A;
        } else if (finance.contains("B")) {
            return Finance.STAGE_B;
        } else if (finance.contains("C")) {
            return Finance.STAGE_C;
        } else if (finance.contains("未")) {
            return Finance.STAGE_NONE;
        } else if (finance.contains("不")) {
            return Finance.STAGE_NONEED;
        }
        return Finance.STAGE_D_OR_PLUS;
    }

}
