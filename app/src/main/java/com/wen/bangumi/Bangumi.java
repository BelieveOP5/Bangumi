package com.wen.bangumi;

import android.app.Application;

/**
 * Created by BelieveOP5 on 2017/1/26.
 */

public class Bangumi extends Application {

    private static Bangumi INSTANCE;

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

    }
}
