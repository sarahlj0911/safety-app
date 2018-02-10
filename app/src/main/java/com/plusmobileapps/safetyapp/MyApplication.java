package com.plusmobileapps.safetyapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by Andrew on 1/17/2018.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

}
