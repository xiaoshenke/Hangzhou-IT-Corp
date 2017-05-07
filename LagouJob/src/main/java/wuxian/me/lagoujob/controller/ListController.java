package wuxian.me.lagoujob.controller;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wuxian.me.lagoujob.Config;
import wuxian.me.lagoujob.mapper.CompanyMapper;
import wuxian.me.lagoujob.mapper.LocationMapper;
import wuxian.me.lagoujob.model.lagou.Company;
import wuxian.me.lagoujob.model.lagou.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wuxian on 6/5/2017.
 */
@Controller
@RequestMapping(value = {"/list"})
//@ComponentScan(value = {"wuxian.me.lagoujob.mapper.lagou"})
public class ListController {

    @Autowired
    public CompanyMapper companyMapper;

    @Autowired
    public LocationMapper locationMapper;

    @RequestMapping
    @ResponseBody
    public Company list(@RequestParam(required = false) String city) {

        if (StringUtils.isEmpty(city)) {
            city = Config.DEFAULT_CITY;
        }

        Company company = companyMapper.loadCompany(Config.TABLE_COMPANY, 749);
        company.calcScore();
        //Todo: locations根据@city来进行帅选
        List<Location> locations = locationMapper.loadLocation(Config.TABLE_LOCATION, 749);
        company.locationList = locations;

        return company;
    }

}
