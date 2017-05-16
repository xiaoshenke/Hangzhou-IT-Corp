package wuxian.me.spidersdk.distribute;

import java.util.Map;

/**
 * Created by wuxian on 12/5/2017.
 * <p>
 * 目前仅支持GET,POST
 */
public final class HttpUrlNode {

    public String baseUrl;

    public Map<String, String> httpGetParam;

    public Map<String, String> httpPostParam;

    public long hash() {
        StringBuilder ret = new StringBuilder(baseUrl);

        if (!httpGetParam.isEmpty()) {
            ret.append("get");
            for (String str : httpGetParam.keySet()) {
                ret.append(str);
            }
        }

        if (!httpPostParam.isEmpty()) {
            ret.append("post");
            for (String str : httpPostParam.keySet()) {
                ret.append(str);
            }
        }

        return ret.toString().hashCode();
    }
}
