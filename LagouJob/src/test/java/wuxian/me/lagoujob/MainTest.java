package wuxian.me.lagoujob;

import okhttp3.*;
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
import wuxian.me.lagoujob.mapper.LocationMapper;
import wuxian.me.lagoujob.mapper.TableMapper;
import wuxian.me.lagoujob.model.Location;

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
    public LocationMapper locationMapper;

    @Autowired
    public TableMapper tableMapper;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    //Fixme:有些数据是不是有问题？
    @Test
    public void testChangeAll() {
        String tableName = "locations";
        Location.tableName = tableName;
        Location location = new Location(106310, "");
        List<Location> locations = locationMapper.loadLocation(location);

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

                System.out.println(result);
                for (int i = 0; i < result.count; i++) {
                    String s = result.geocodes.get(i).location;
                    String[] ss = s.split(",");

                    if (ss != null && ss.length == 2) {

                        location.longitude = ss[0];
                        location.lantitude = ss[1];
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
            mockMvc.perform(MockMvcRequestBuilders.get("/home"))
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