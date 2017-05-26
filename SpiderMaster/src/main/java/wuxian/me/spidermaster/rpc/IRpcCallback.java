package wuxian.me.spidermaster.rpc;

/**
 * Created by wuxian on 26/5/2017.
 * <p>
 * Todo:
 */
public interface IRpcCallback {

    void onSent();

    void onResponseSuccess();

    void onResponseFail();

}
