package wuxian.me.spidersdk.util;

import com.sun.istack.internal.NotNull;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import wuxian.me.spidersdk.log.LogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxian on 15/5/2017.
 */
public class ProcessSignalManager implements SignalHandler {

    private List<OnSystemKill> killList = new ArrayList<OnSystemKill>();

    //系统的支持程度各异
    public ProcessSignalManager() {
        Signal.handle(new Signal("TERM"), this);  // kill -15 common kill
        Signal.handle(new Signal("INT"), this);   // Ctrl+c
        Signal.handle(new Signal("KILL"), this);  // kill -9  no Support
        Signal.handle(new Signal("USR1"), this);   // kill -10
        Signal.handle(new Signal("USR2"), this);   // kill -12
    }


    public void init() {

    }

    //Todo
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
