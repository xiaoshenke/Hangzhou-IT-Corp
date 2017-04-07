package wuxian.me.lagouspider.area;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.model.Area;

/**
 * Created by wuxian on 7/4/2017.
 */
public class AreaPageSpider implements Runnable {

    private Area area;
    private int pageIndex;

    public AreaPageSpider(@NotNull Area area, @NotNull int pageNum) {
        this.area = area;
        this.pageIndex = pageNum;
    }

    public void run() {
        beginSpider();
    }

    //Todo:
    private void beginSpider() {
        ;
    }
}
