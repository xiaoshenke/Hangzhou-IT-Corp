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

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("BaseUrl: " + baseUrl);

        if (!httpGetParam.isEmpty()) {
            ret.append(" GetParam: ");
            for (String str : httpGetParam.keySet()) {
                ret.append(str + ":");
                ret.append(httpGetParam.get(str) + " ");
            }
        }

        if (!httpPostParam.isEmpty()) {
            ret.append(" PostParam: ");
            for (String str : httpPostParam.keySet()) {
                ret.append(str + ":");
                ret.append(httpPostParam.get(str) + " ");
            }
        }

        return ret.toString();
    }

    public long toPatternKey() {
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

    public long toRedisKey() {
        StringBuilder ret = new StringBuilder(baseUrl);

        if (!httpGetParam.isEmpty()) {
            ret.append("get");
            for (String str : httpGetParam.keySet()) {
                ret.append(str);
                ret.append(httpGetParam.get(str));
            }
        }

        if (!httpPostParam.isEmpty()) {
            ret.append("post");
            for (String str : httpPostParam.keySet()) {
                ret.append(str);
                ret.append(httpPostParam.get(str));
            }
        }

        return ret.toString().hashCode();
    }
}
