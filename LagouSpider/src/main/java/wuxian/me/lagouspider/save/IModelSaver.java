package wuxian.me.lagouspider.save;

import com.sun.istack.internal.NotNull;
import wuxian.me.lagouspider.model.Company;

/**
 * Created by wuxian on 8/4/2017.
 */
public interface IModelSaver<T> {
    /**
     * @return datachanged
     */
    boolean saveModel(@NotNull T model);
}
