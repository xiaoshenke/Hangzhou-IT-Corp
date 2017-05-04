package wuxian.me.lagoujob.geoapi;

import java.util.List;

/**
 * Created by wuxian on 2/5/2017.
 */
public class GeoResult {

    public int httpcode = 200;

    public int status;

    public String info;

    public int infocode;

    public int count;

    public List<GeoCode> geocodes;

    public static class GeoCode {
        //public String formatted_address;
        public String province;
        public String citycode;
        public String city;
        public String district;
        public String location;
        public String level;

        @Override
        public String toString() {
            return "GeoCode{" +
                    "city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    ", location='" + location + '\'' +
                    ", level='" + level + '\'' +
                    '}';
        }
    }

    public boolean isSuccess() {
        return httpcode == 200 && status == 1;
    }

    //http请求是否成功
    public boolean isHttpRequestOk() {
        return httpcode == 200;
    }

    @Override
    public String toString() {
        return "GeoResult{" +
                "httpcode=" + httpcode +
                ", status=" + status +
                ", info='" + info + '\'' +
                ", infocode=" + infocode +
                ", count=" + count +
                ", geocodes=" + geocodes +
                '}';
    }
}
