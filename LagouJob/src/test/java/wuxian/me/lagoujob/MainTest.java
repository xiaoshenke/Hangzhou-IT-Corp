package wuxian.me.lagoujob;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
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
import wuxian.me.lagoujob.util.OkhttpProvider;

import java.io.IOException;
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

    @Test
    public void testTable() {
        List<String> tables = tableMapper.showTables();

        if (tables != null) {
            for (String tabel : tables) {
                System.out.println(tabel);
            }
        }
    }

    @Test
    public void testLocation() {

        String tableName = "locations";
        Location.tableName = tableName;
        System.out.println("Hello");

        List<Location> locations = locationMapper.loadAll(tableName);

        if (locations != null) {
            for (Location location : locations) {
                System.out.println(location.name());  //company_id:40823
                try {
                    GeoResult result = GeoService.sendRequest(location.location);

                    if (result.isSuccess()) {
                        String s = result.geocodes.get(0).location;
                        String[] ss = s.split(",");
                        if (ss != null && ss.length == 2) {
                            location.longitude = ss[0];
                            location.lantitude = ss[1];

                            locationMapper.updateLocation(location);
                            System.out.print("Success");
                        }
                    }
                } catch (IOException e) {
                    ;
                }

                break;
            }
        }
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

}