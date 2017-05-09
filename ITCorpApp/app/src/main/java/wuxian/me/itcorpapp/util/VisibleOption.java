package wuxian.me.itcorpapp.util;

/**
 * Created by wuxian on 9/5/2017.
 * <p>
 * used at @Company
 */

public class VisibleOption {

    public static int MODE_EQUALS = 0;
    public static int MODE_GREATER_THAN = 1;
    public static int MODE_LESS_THAN = 2;
    public static int MODE_GREATER_THAN_INCLUDE = 3;

    public static VisibleOption DEFAULT;

    static {
        DEFAULT = new VisibleOption();
        DEFAULT.stage = MODE_GREATER_THAN_INCLUDE;
        DEFAULT.cameraZoom = 10.0f;
    }

    public int stage;

    //模式：只显示该stage,高于该stage,低于该stage
    public int mode = MODE_EQUALS;

    public float cameraZoom;


}
