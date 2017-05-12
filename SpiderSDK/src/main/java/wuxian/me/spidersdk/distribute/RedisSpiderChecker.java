package wuxian.me.spidersdk.distribute;

/**
 * Created by wuxian on 12/5/2017.
 */
public class RedisSpiderChecker {

    private RedisSpiderChecker() {
    }

    //Todo:如果有类不合法 throw RuntimeException
    //Todo:扫描包下的@BaseSpider的子类 若没有实现fromUrlNode,toUrlNode,报错
    public static void performCheck() {
        ;
    }
}
