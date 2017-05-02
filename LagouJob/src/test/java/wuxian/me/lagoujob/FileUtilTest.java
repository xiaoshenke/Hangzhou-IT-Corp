package wuxian.me.lagoujob;


import org.junit.Test;

import wuxian.me.lagoujob.geoapi.GeoResult;
import wuxian.me.lagoujob.geoapi.GeoService;


import java.io.IOException;

import static org.junit.Assert.*;
import static wuxian.me.lagoujob.geoapi.Api.*;

/**
 * Created by wuxian on 2/5/2017.
 */
public class FileUtilTest {

    @Test
    public void testGEO() {
        String address = "杭州西湖区杭州市西湖区西溪路550号西溪新座6幢A座301";
        try {
            GeoResult result = GeoService.sendRequest(address);
            if (result.isHttpRequestOk()) {
                if (result.isSuccess()) {
                    System.out.println(result.geocodes.get(0).location);
                } else {
                    System.out.println(result.info);
                }
            } else {
                System.out.println(result.httpcode);
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

}