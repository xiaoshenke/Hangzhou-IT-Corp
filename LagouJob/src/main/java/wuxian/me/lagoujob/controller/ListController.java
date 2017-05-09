package wuxian.me.lagoujob.controller;

import com.google.common.collect.Collections2;
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
import wuxian.me.lagoujob.util.BaseCompanyFilter;
import wuxian.me.lagoujob.util.GeoFilter;
import wuxian.me.lagoujob.util.Helper;
import wuxian.me.lagoujob.util.NoLocationCompanyFilter;

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
    public List<Company> list(@RequestParam(required = false) String city) {

        if (StringUtils.isEmpty(city)) {
            city = Config.DEFAULT_CITY;
        }

        /*
        Company company = companyMapper.loadCompany(Config.TABLE_COMPANY, 749);
        company.calcScore();
        List<Location> locations = locationMapper.loadLocation(Config.TABLE_LOCATION, 749);
        company.locationList = locations;
        */

        List<Company> companyList = companyMapper.loadAll(Config.TABLE_COMPANY, Config.TABLE_LOCATION);

        companyList = Helper.filterAndReturn(companyList, new BaseCompanyFilter());

        for (Company company : companyList) {
            company.locationList = Helper.filterAndReturn(company.locationList, new GeoFilter());
        }

        //Fixme:这说明这家单位在杭州只是一个分部 或者在拉勾上根本就没有location... 可以直接filter掉么？
        //companyList = Helper.filterAndReturn(companyList,new NoLocationCompanyFilter());

        return companyList;
    }


}
