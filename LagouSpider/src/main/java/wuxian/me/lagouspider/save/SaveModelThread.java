package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;
import static wuxian.me.lagouspider.util.ModuleProvider.logger;

/**
 * Created by wuxian on 8/4/2017.
 * Todo: better log
 */
public class SaveModelThread<T> extends Thread {

    private Map<Long, T> model;
    private int interval;
    private boolean insert;
    private IDatabaseOperator<T> operator;

    public SaveModelThread(@NotNull Map<Long, T> model, int interval,
                           IDatabaseOperator<T> operator) {
        this(model, interval, operator, true);
    }

    public SaveModelThread(@NotNull Map<Long, T> model, int interval,
                           IDatabaseOperator<T> operator, boolean insert) {
        this.model = model;
        this.interval = interval;
        this.insert = insert;
        this.operator = operator;
    }

    @Override
    public void run() {
        while (true) {
            if (!model.isEmpty()) {
                Map<Long, T> modelMap;

                synchronized (model) {
                    modelMap = new HashMap<Long, T>(model);
                    model.clear();
                }

                for (T model : modelMap.values()) {
                    if (insert) {
                        logger().info("SaveModelThread: Insert");

                        if (operator != null) {
                            operator.insert(model);
                        }

                    } else {
                        logger().info("SaveModelThread: Update");
                        if (operator != null) {
                            operator.update(model);
                        }
                    }
                }
            }

            try {
                sleep(interval);
            } catch (InterruptedException e) {
                ;
            }
        }
    }

    public interface IDatabaseOperator<T> {
        void insert(T model);

        void update(T model);
    }
}
