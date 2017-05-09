package wuxian.me.lagoujob.util;

import com.google.common.base.Predicate;
import wuxian.me.lagoujob.model.lagou.Company;

/**
 * Created by wuxian on 9/5/2017.
 */
public class NoLocationCompanyFilter implements Predicate<Company> {
    public boolean apply(Company company) {

        if (company.locationList != null && company.locationList.size() != 0) {
            return true;
        }

        return false;
    }

    public boolean test(Company input) {
        return apply(input);
    }
}
