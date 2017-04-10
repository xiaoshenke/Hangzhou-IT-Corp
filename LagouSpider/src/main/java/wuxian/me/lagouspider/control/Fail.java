package wuxian.me.lagouspider.control;

/**
 * Created by wuxian on 7/4/2017.
 */
public class Fail {

    private static final int FAIL_NETWORK_ERROR = -1;
    private static final int FAIL_BLOCK_404 = 101;

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

    @Override
    public String toString() {
        if (httpCode == FAIL_NETWORK_ERROR) {
            return "network error";
        } else if (httpCode == FAIL_BLOCK_404) {
            return "ip blocked by Lagou-Anti-Spider";
        }
        return "httpcode " + httpCode + " ,message " + message;
    }

    public boolean isNetworkErr() {
        return httpCode == FAIL_NETWORK_ERROR;
    }

    public boolean isBlock() {
        return httpCode == FAIL_BLOCK_404;
    }

    public final static Fail BLOCK = new Fail(FAIL_BLOCK_404, "Blocked");

    public final static Fail NETWORK_ERR = new Fail(FAIL_NETWORK_ERROR, "NetworkErr");
}
