package wuxian.me.spidercommon.model;

/**
 * Created by wuxian on 12/6/2017.
 */
public class Proxy {

    public String ip;
    public int port;

    public Proxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proxy proxy = (Proxy) o;

        if (port != proxy.port) return false;
        return ip != null ? ip.equals(proxy.ip) : proxy.ip == null;

    }

    //标准hashcode的实现
    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }
}
