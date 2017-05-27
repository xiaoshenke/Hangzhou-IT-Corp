package wuxian.me.spidermaster.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuxian on 26/5/2017.
 */
public class IpPortUtil {

    private IpPortUtil() {
    }

    public static boolean isValidIpPort(String ipport) {
        if (ipport == null || ipport.length() == 0) {
            return false;
        }

        return isVaildIpAndPort(ipport.split(":"));
    }

    public static boolean isVaildIpAndPort(String[] ipport) {
        if (ipport == null || ipport.length != 2) {
            return false;
        }

        String reg1 = "[0-9]+[.][0-9]+[.][0-9]+[.][0-9]+";
        Pattern pattern = Pattern.compile(reg1);
        Matcher matcher = pattern.matcher(ipport[0]);
        if (!matcher.matches()) {
            return false;
        }

        String reg2 = "[0-9]+";
        pattern = Pattern.compile(reg2);
        matcher = pattern.matcher(ipport[1]);
        return matcher.matches();
    }
}
