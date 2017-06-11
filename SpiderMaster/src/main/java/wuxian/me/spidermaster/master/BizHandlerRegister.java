package wuxian.me.spidermaster.master;

import com.sun.istack.internal.Nullable;
import wuxian.me.spidermaster.master.biz.IBizHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuxian on 11/6/2017.
 * <p>
 */
public class BizHandlerRegister {

    private static Map<String, IBizHandler> handlerMap = new HashMap<String, IBizHandler>();

    private BizHandlerRegister() {
    }


    public static <T extends IBizHandler> void registerBizHandler(T handler) {
        if (handler == null) {
            return;
        }
        RpcBizHandler annotation = (handler.getClass().getAnnotation(RpcBizHandler.class));
        if (annotation == null) {
            return;
        }

        String method = annotation.methodName();
        if (method == null || method.length() == 0) {  //not valid
            return;
        }

        if (handlerMap.containsKey(method)) {
            return;
        }

        handlerMap.put(method, handler);

    }

    @Nullable
    public static IBizHandler getHandlerBy(String methodName) {
        if (methodName == null || methodName.length() == 0) {
            return null;
        }
        return handlerMap.get(methodName);
    }

}