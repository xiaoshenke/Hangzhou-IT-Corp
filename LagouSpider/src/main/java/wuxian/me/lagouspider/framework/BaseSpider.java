package wuxian.me.lagouspider.framework;

import okhttp3.Request;
import okhttp3.Response;
import wuxian.me.lagouspider.util.Helper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wuxian on 13/4/2017.
 */
public abstract class BaseSpider implements Runnable {

    public static final int RET_SUCCESS = 0; //成功
    public static final int RET_ERROR = 1;   //失败(不是因为解析失败造成的失败)
    public static final int RET_PARSING_ERR = 2;//parsing错误造成的失败

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat fullLogSdf = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");

    private SpiderCallback callback;
    private Request request;

    public BaseSpider() {
        callback = new SpiderCallback(this);
    }

    public final void run() {
        OkhttpProvider.getClient().newCall(getRequest()).enqueue(callback);
    }


    private final Request getRequest() {
        if (request == null) {
            request = buildRequest();
        }
        return request;
    }

    protected abstract Request buildRequest();

    /**
     * 即使HttpCode返回200 页面也有可能是伪造的或者数据是造假的或者是一个You are blocked(Fuck you spider)页面
     *
     * @return state
     * 若返回 @RET_SUCCESS,认为本次抓取成功
     * 若返回 @RET_ERROR,认为本次抓取失败
     * 若返回 @RET_PARSING_ERR,可能是被屏蔽了 由@checkBlockAndFailThisSpider(String html)决定是否被屏蔽
     */
    public abstract int parseRealData(String data);


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

    //将详细错误码包括Http Request,Response写到本地文件
    public final void serializeFullLog() {
        StringBuilder builder = new StringBuilder("");
        Date date = new Date();
        String time = sdf.format(date);
        builder.append(time);

        builder.append(" [" + Thread.currentThread().getName() + "]/n");
        builder.append("Spider: " + toString() + "/n");

        builder.append("Request: " + buildRequest() + "/n");
        Response response = callback.getResponse();
        if (response != null) {
            builder.append("Response: HttpCode: " + response.code() + " isRedirect: " + response.isRedirect() + " Message: " + response.message() + "/n");
            builder.append("Header: " + response.headers().toString() + "/n");

            try {
                builder.append("/nBody: " + response.body().string());
            } catch (IOException e) {
                if (response.body() != null) {
                    response.body().close();
                }
            }
        }
        String fileName = fullLogSdf.format(date) + simpleName(); //simpleName只有一个类名

        //Fixme:framework包下引入了一个非framework的Helper类 --> 间接引用了一些配置项 不过这个类移植起来还是很容易的
        FileUtil.writeToFile(Helper.getFullLogFilePath(fileName), builder.toString());
    }

}
