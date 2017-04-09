package wuxian.me.lagouspider.control;

/**
 * Created by wuxian on 7/4/2017.
 */
public class Fail {

    public static final int FAIL_NETWORK_ERROR = -1;

    int httpCode;
    String message;

    long millis;

    public Fail(int code, String msg) {
        this.httpCode = code;
        this.message = msg;

        millis = System.currentTimeMillis();
    }

    public Fail(int code) {
        this(code, "");
    }
}
