package wuxian.me.itcorpapp.util;

import android.support.annotation.NonNull;

import wuxian.me.itcorpapp.model.Company;
import wuxian.me.itcorpapp.model.Finance;

/**
 * Created by wuxian on 9/5/2017.
 */

public class VisibleUtil {

    private VisibleUtil() {
        ;
    }

    //Fixme:
    public static boolean isVisible(@NonNull Company company,
                                    @NonNull VisibleOption option) {
        float zoom = option.cameraZoom;
        //zoom10以下 只显示D轮以上
        if (zoom <= 11.0f) {
            return company.stage >= Finance.STAGE_D_OR_PLUS.getValue();
        }

        if (zoom > 11.0f && zoom <= 15.0f) {
            return company.stage >= Finance.STAGE_B.getValue();
        }
        return true;
    }

    //Todo
    public static float getZIndexOf(@NonNull Company company){
        return 1f;
    }
}
