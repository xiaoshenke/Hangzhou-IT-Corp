package wuxian.me.lagouspider.core;

import wuxian.me.lagouspider.model.Area;

/**
 * Created by wuxian on 30/3/2017.
 * <p>
 * Todo：抓取该公司所有的职位
 */
public class CompanySpider implements Runnable {
    Area area;

    public CompanySpider(Area area) {
        this.area = area;
    }

    public void run() {

    }
}
