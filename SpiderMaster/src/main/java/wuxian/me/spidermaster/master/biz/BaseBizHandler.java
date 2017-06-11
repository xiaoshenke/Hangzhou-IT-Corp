package wuxian.me.spidermaster.master.biz;


import wuxian.me.spidermaster.master.core.RpcBizHandler;

/**
 * Created by wuxian on 11/6/2017.
 */
public abstract class BaseBizHandler implements IBizHandler {

    public final String getMethodName() {
        RpcBizHandler annotation = (getClass().getAnnotation(RpcBizHandler.class));
        if (annotation == null) {
            return "";
        }

        String method = annotation.methodName();
        if (method == null) {  //not valid
            return "";
        }

        return method;
    }


}
