package wuxian.me.lagouspider.biz.lagou;

/**
 * Created by wuxian on 25/7/2017.
 */
public enum HangzhouDistrict {

    Xihu("西湖区"),
    Binjiang("滨江区"),
    Yuhang("余杭区"),
    Gongshu("拱墅区"),
    Jianggan("江干区"),
    Xiacheng("下城区"),
    Xiaoshan("萧山区"),
    Shangcheng("上城区"),
    Xiasha("下沙区"),;

    private String name;

    HangzhouDistrict(String name) {
        this.name = name;
    }

    public String district() {
        return name;
    }
}
