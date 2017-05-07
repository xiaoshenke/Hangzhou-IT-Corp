package wuxian.me.lagoujob.util;

import com.google.common.base.Predicate;
import wuxian.me.lagoujob.model.lagou.Company;

/**
 * Created by wuxian on 7/5/2017.
 */
public class CompanyFilter implements Predicate<Company> {

    public boolean test(Company input) {
        return apply(input);
    }

    public boolean apply(Company company) {

        //没有融资信息直接过滤
        if (company.financeStage == null) {
            return false;
        }

        //没有职位信息直接过滤？？
        if (company.positionNum == null) {
            return false;
        }

        //拉勾未认证且没有面试打分信息 基本可以确定是家没什么价值的公司 过滤
        if (company.lagouAuthentic == null || Boolean.parseBoolean(company.lagouAuthentic) == false) {
            if (company.interviewScore == null) {
                return false;
            }
        }  //剩下的那些虽然没有面试打分信息但是被拉勾认证的 可能是土豪公司...


        return true;
    }
}
