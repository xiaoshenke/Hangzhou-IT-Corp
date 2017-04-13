package wuxian.me.lagouspider.framework;

/**
 * Created by wuxian on 13/4/2017.
 */
public abstract class BaseSpider implements Runnable {

    public static final int RET_SUCCESS = 0; //成功
    public static final int RET_ERROR = 1;   //失败(不是因为解析失败造成的失败)
    public static final int RET_PARSING_ERR = 2;//parsing错误造成的失败

    //根据HttpCode判定是否被block
    public abstract boolean checkBlockAndFailThisSpider(int httpCode);

    //根据网页内容判定是否被block
    protected abstract boolean checkBlockAndFailThisSpider(String html);

    public final String simpleName() {
        return getClass().getSimpleName();
    }

    //For Log
    public abstract String name();

    public abstract String fullName();


    /**
     * 即使HttpCode返回200 页面也有可能是伪造的或者数据是造假的或者是一个You are blocked(Fuck you spider)页面
     *
     * @return state
     * 若返回 @RET_SUCCESS,认为本次抓取成功
     * 若返回 @RET_ERROR,认为本次抓取失败
     * 若返回 @RET_PARSING_ERR,可能是被屏蔽了 由@checkBlockAndFailThisSpider(String html)决定是否被屏蔽
     */
    public abstract int parseRealData(String data);

    //将详细错误码包括Http Request,Response写到本地文件
    public abstract void serializeFullLog();
}
