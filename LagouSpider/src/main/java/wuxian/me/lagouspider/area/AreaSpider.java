package wuxian.me.lagouspider.area;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.model.Area;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * Todo:抓取某块区域所有的公司
 * 1 先拿到该地区的页数
 * 2 根据页数发送每一页数据的请求
 * 3 重试机制
 */
public class AreaSpider implements Runnable {
    Area area;
    private int pageNum;

    public AreaSpider(@NotNull Area area) {
        this.area = area;
    }

    //Todo
    public void beginSpider() {
        getCompanyPageNum();
    }

    private void getCompanyPageNum() {
        ;
    }

    public void run() {
        beginSpider();
    }
}
