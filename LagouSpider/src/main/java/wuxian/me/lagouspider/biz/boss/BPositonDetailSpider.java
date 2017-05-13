package wuxian.me.lagouspider.biz.boss;

import okhttp3.Request;

/**
 * Created by wuxian on 13/5/2017.
 * <p>
 * Todo:
 */
public class BPositonDetailSpider extends BaseBossSpider {

    private long positionId;

    public BPositonDetailSpider(long positionId) {
        this.positionId = positionId;
    }

    protected Request buildRequest() {
        return null;
    }

    public int parseRealData(String s) {
        return 0;
    }

    public String name() {
        return "BossPositionDetailSpider: positionId: " + positionId;
    }
}
