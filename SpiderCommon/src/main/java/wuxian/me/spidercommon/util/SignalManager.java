package wuxian.me.spidercommon.util;

import com.sun.istack.internal.NotNull;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import wuxian.me.spidercommon.log.LogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 11/6/2017.
 */
public class SignalManager implements SignalHandler {

    private List<OnSystemKill> killList = new ArrayList<OnSystemKill>();

    //系统的支持程度各异
    public SignalManager() {
    }

    private void listenToSignal(String sig) {
        try {
            Signal.handle(new Signal(sig), this);
        } catch (Exception e) {
            LogManager.error("SignalManager " + sig + ": " + e.getMessage());
        }
    }


    public void init() {
        listenToSignal("TERM");
        listenToSignal("INT");
        listenToSignal("KILL");
        listenToSignal("USR1");
        listenToSignal("USR2");
    }

    public void handle(Signal signal) {
        LogManager.info("Signal: " + signal.getName());
        for (OnSystemKill onSystemKill : killList) {
            onSystemKill.onSystemKilled();
        }
    }

    public void registerOnSystemKill(@NotNull OnSystemKill onSystemKill) {
        if (!killList.contains(onSystemKill)) {
            killList.add(onSystemKill);
        }
    }

    public interface OnSystemKill {
        void onSystemKilled();
    }
}


