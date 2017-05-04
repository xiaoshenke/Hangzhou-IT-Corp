package wuxian.me.itcorpapp.map;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.squareup.picasso.Picasso;

/**
 * Created by wuxian on 4/5/2017.
 */

public class MarkerUtil {

    //Todo:自定义marker图片 关键是这里的image loader问题...
    //准备使用数据库里的公司头像 因此会比较麻烦一点
    MarkerOptions getOptions() {
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(120.125809, 30.272553)); //支付宝 company_id:749

        //options.icon(BitmapDescriptorFactory.)
        return options;
    }
}
