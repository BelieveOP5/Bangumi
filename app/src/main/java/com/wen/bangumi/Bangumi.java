package com.wen.bangumi;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.wen.bangumi.greenDAO.DaoMaster;
import com.wen.bangumi.greenDAO.DaoSession;

/**
 * Created by BelieveOP5 on 2017/1/26.
 */

public class Bangumi extends Application {

    private static Bangumi INSTANCE;

    private DaoSession mDaoSession;

    /**
     * 获取实例
     * @return
     */
    public static Bangumi getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Bangumi();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        //设置数据库连接
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "bangumi-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        mDaoSession = new DaoMaster(db).newSession();

    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
