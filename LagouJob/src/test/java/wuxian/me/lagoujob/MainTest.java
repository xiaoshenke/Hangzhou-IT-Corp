package wuxian.me.lagoujob;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wuxian.me.lagoujob.geoapi.GeoResult;
import wuxian.me.lagoujob.geoapi.GeoService;
import wuxian.me.lagoujob.mapper.CompanyMapper;
import wuxian.me.lagoujob.mapper.LocationMapper;
import wuxian.me.lagoujob.mapper.TableMapper;
import wuxian.me.lagoujob.model.lagou.Company;
import wuxian.me.lagoujob.model.lagou.Location;
import wuxian.me.lagoujob.util.BaseCompanyFilter;
import wuxian.me.lagoujob.util.GeoFilter;
import wuxian.me.lagoujob.util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 1/5/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "web")
@ContextHierarchy({
        @ContextConfiguration("/applicationContext.xml"),
        @ContextConfiguration("file:web/WEB-INF/spring-mvc.xml"),
        @ContextConfiguration("/applicationContext-datasource.xml"),

})
public class MainTest {
    private MockMvc mockMvc;

    @Autowired
    public CompanyMapper companyMapper;

    @Autowired
    public LocationMapper locationMapper;

    @Autowired
    public TableMapper tableMapper;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testListControl() {
        List<Company> companyList;//= companyMapper.loadCompany(Config.TABLE_COMPANY, Config.TABLE_LOCATION,55046);
        companyList = companyMapper.loadAll(Config.TABLE_COMPANY, Config.TABLE_LOCATION);

        companyList = Helper.filterAndReturn(companyList, new BaseCompanyFilter());

        for (Company company : companyList) {
            company.locationList = Helper.filterAndReturn(company.locationList, new GeoFilter());

            print2(company);
        }

    }

    private void print2(Company company) {
        System.out.println("Company: {id:" + company.company_id + " name:"
                + company.name + " locationNum: " +
                (company.locationList == null ? 0 : company.locationList.size()) + "}");

        for (Location location : company.locationList) {
            System.out.println(location.name());
        }
    }

    @Test
    public void testCompanyDB() {
        String tb = "companies";
        //Company company = companyMapper.loadCompany(tb, 749);
        //System.out.println(company);

        List<Company> list = companyMapper.loadCompany("companies", "locations", 191131);

        for (Company company : list) {
            System.out.println(company);
        }

        /*
        List<Company> list = companyMapper.loadAllCompanies(tb);
        list = Helper.fromCollection(Collections2.filter(list, new BaseCompanyFilter()));

        for (Company company : list) {
            company.score = ScoreUtil.calScoreFieldOf(company);
            print(company);
        }
        */
    }

    private void print(Company company) {
        System.out.println("Company: {id:" + company.company_id + " name:"
                + company.name + " financeStage:" + company.financeStage
                + "}");
        System.out.println("Score:" + company.score + " position:" + company.positionNum
                + " positionScore:" + company.positionNumScore + " interview:"
                + company.interviewScore + " interviewScore:" + company.interScore
                + " financeScore:" + company.finaceScore + " authenScore:" + company.authenScore);
        System.out.println();
    }

    @Test
    public void testChangeAll() {
        String tableName = "locations";
        Location.tableName = tableName;
        List<Location> locations = locationMapper.loadAll(tableName);

        for (Location location1 : locations) {
            batchChange(location1);
        }

        while (true) {

        }
    }

    private void batchChange(final Location location) {
        List<String> addressList = new ArrayList<String>();
        addressList.add(location.location);

        GeoService.asyncSendRequest(addressList, new GeoService.IGeoResultCallback() {
            public void onResult(GeoResult result) {
                System.out.println("" + location.locationId + result);

                if (result.count == 0) {
                    location.longitude = "-1";
                    location.lantitude = "-1";

                    locationMapper.updateLocation(location);
                }

                for (int i = 0; i < result.count; i++) {
                    String s = result.geocodes.get(i).location;
                    String[] ss = s.split(",");

                    if (ss != null && ss.length == 2) {

                        location.longitude = ss[0];
                        location.lantitude = ss[1];

                        locationMapper.updateLocation(location);
                    } else {
                        location.longitude = "-1";
                        location.lantitude = "-1";

                        locationMapper.updateLocation(location);
                    }
                }

            }

            public void onNetFail() {
            }
        });
    }


    @Test
    public void testMockMVC() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/list"))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            //logger.error("exception");
        }
    }

    @Test
    public void testTable() {
        List<String> tables = tableMapper.showTables();

        if (tables != null) {
            for (String tabel : tables) {
                System.out.println(tabel);
            }
        }
    }

}