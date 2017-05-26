package wuxian.me.spidermaster.rpc;

/**
 * Created by wuxian on 26/5/2017.
 */
public class RpcRequest {

    public String requestId;

    public String className;

    public String methodName;

    public Class<?>[] parameterTypes;

    public Object[] parameters;
}
