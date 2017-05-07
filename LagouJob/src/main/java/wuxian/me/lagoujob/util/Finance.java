package wuxian.me.lagoujob.util;

/**
 * Created by wuxian on 7/5/2017.
 */
public enum Finance {
    //天使
    STAGE_TIANSHI(1),

    //A轮
    STAGE_A(2),

    //未融资 --> 可能存在土豪
    STAGE_NONE(3),

    //未融资 --> 可能存在土豪
    STAGE_NONEED(4),

    STAGE_B(5),

    STAGE_C(6),

    //D轮或以上
    STAGE_D_OR_PLUS(7),

    //上市
    STAGE_SHANGSHI(8);

    private Finance(int stage) {
        this.stage = stage;
    }

    public int getValue() {
        return stage;
    }

    private int stage;

}
