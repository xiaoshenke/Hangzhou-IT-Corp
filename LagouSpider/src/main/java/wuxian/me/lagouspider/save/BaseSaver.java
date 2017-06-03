package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.spidersdk.util.ProcessManager;

/**
 * Created by wuxian on 3/6/2017.
 */
public abstract class BaseSaver<T> implements IModelSaver<T>, ProcessManager.OnSystemKill {

    @NotNull
    protected abstract Thread getSaverThread();

    @Override
    public final void onSystemKilled() {
        Thread thread = getSaverThread();
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {

        }
    }
}
