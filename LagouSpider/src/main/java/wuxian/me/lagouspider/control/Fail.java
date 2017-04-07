package wuxian.me.lagouspider.control;

/**
 * Created by wuxian on 7/4/2017.
 */
public class Fail {
    int httpCode;
    String message;

    public Fail(int code, String msg) {
        this.httpCode = code;
        this.message = msg;
    }

    public Fail(int code) {
        this(code, "");
    }
}
