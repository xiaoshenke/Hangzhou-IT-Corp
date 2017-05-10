package wuxian.me.itcorpapp.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import wuxian.me.itcorpapp.model.CompanyDao;
import wuxian.me.itcorpapp.model.DaoMaster;
import wuxian.me.itcorpapp.model.DaoSession;
import wuxian.me.itcorpapp.model.LocationDao;

/**
 * Created by wuxian on 9/5/2017.
 * <p>
 * reference:http://www.jianshu.com/p/4986100eff90
 * https://github.com/greenrobot/greenDAO
 * http://greenrobot.org/greendao/documentation/modelling-entities/
 * <p>
 * Fixme:GreenDao似乎不支持修改表的名称...
 */

public class GreenDaoHelper {

    private static DaoMaster.DevOpenHelper helper;
    private static SQLiteDatabase writeDb;
    private static DaoSession session;

    private GreenDaoHelper() {
    }

    public static void init(Context context) {
        helper = new DaoMaster.DevOpenHelper(context, "itcorpapp-db", null);
        writeDb = helper.getWritableDatabase();
        session = new DaoMaster(writeDb).newSession();
    }

    public static CompanyDao companyDao() {
        return session.getCompanyDao();
    }

    public static LocationDao locationDao() {
        return session.getLocationDao();
    }
}
