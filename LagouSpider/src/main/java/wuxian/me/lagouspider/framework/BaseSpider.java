package wuxian.me.lagouspider.framework;

import com.sun.istack.internal.NotNull;
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

    private static final String LINTFEED = "/r/n";
    public static final int RET_SUCCESS = 0; //成功

    //parsing error --> 指new Parser(html) 是不是因为parser能力太弱 这个网页有些tag不支持
    //这种情况认为任务失败了(FullLog处理),但不进行重试
    public static final int RET_PARSING_ERR = 1;

    public static final int RET_MAYBE_BLOCK = 2; //可能被block

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat fullLogSdf = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");

    private SpiderCallback callback;
    private Request request;

    public BaseSpider() {
        callback = getCallback();
    }

    @NotNull
    protected abstract SpiderCallback getCallback();

    public final void run() {
        Request request = getRequest();
        OkhttpProvider.getClient().newCall(request).enqueue(callback);
    }


    private final Request getRequest() {
        if (request == null) {
            request = buildRequest();
        }
        return request;
    }

    @NotNull
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
    protected boolean checkBlockAndFailThisSpider(int httpCode) {
        if (httpCode == -1 || httpCode == 404) {
            return true;
        }
        return false;
    }

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

        builder.append(" [" + Thread.currentThread().getName() + "]" + LINTFEED);
        builder.append("Spider: " + toString() + LINTFEED);

        builder.append("Request: " + buildRequest() + LINTFEED);
        Response response = callback.getResponse();
        if (response != null) {
            builder.append("Response: HttpCode: " + response.code() + " isRedirect: " + response.isRedirect() + " Message: " + response.message() + LINTFEED);
            builder.append("Header: " + response.headers().toString() + LINTFEED);
            builder.append(LINTFEED + "Body: " + callback.getBody());
        }

        String name = name().length() > 15 ? name().substring(0, 15) : name();
        String fileName = fullLogSdf.format(date) + name; //simpleName只有一个类名

        FileUtil.writeToFile(Helper.getFullLogFilePath(fileName), builder.toString());
    }

}
