package wuxian.me.lagouspider.framework;

/**
 * Created by wuxian on 14/4/2017.
 * <p>
 * html节点和org.htmlparser对应表
 * <p>
 * 且注意devTools看到的源码和网页源码可能是不一致的,以后者为准 不得不说java的解析器不够强大...写着费劲
 */
public interface Converter {

    //<li /> --> Bullet.class

    //<a href="xxx" /> --> LinkTag

    //<p /> --> TextNode
}
