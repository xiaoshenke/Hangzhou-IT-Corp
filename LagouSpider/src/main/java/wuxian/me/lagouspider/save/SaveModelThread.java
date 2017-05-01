package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.model.BaseModel;
import wuxian.me.spidersdk.log.LogManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by wuxian on 8/4/2017.
 */
public class SaveModelThread<T extends BaseModel> extends Thread {

    private Set<Long> savedModel = new HashSet<Long>();  //解决插入重复的值 Fixme:如何防止二次插入？？
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

                Class claz = null;
                for (T model : modelMap.values()) {
                    if (claz == null) {
                        claz = model.getClass();
                        if (insert) {
                            LogManager.info(getName() + "-->Insert count: " + modelMap.values().size());
                        } else {
                            LogManager.info(getName() + "-->Update count: " + modelMap.values().size());
                        }
                    }

                    if (operator != null) {
                        if (insert) {
                            try {
                                operator.insert(model);
                            } catch (Exception e) {
                                LogManager.error(getName() + " insertModel: " + model.name() + " error: " + e.getMessage() + " we will ignore");
                            }

                        } else {
                            try {
                                operator.update(model);
                            } catch (Exception e) {
                                LogManager.error(getName() + " updateModel: " + model.name() + " error: " + e.getMessage() + " we will ignore");
                            }

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
