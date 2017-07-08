package wuxian.me.lagouspider.biz.lagou;

/**
 * Created by wuxian on 8/7/2017.
 */
public enum FinanceEnum {

    TIANSHI(0),
    A(1),
    B(2),
    C(3),
    D(4),
    WEI_RONGZI(5),
    NO_NEED(6),
    SHANGSHI(7);

    private int stage;

    FinanceEnum(int stage) {

        this.stage = stage;
    }

    public int stage() {
        return stage;
    }
}
