package com.plusmobileapps.safetyapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Robert on 11/18/2017.
 * Thanks to https://www.androidhive.info/2016/05/android-build-intro-slider-app/
 */

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "safetyapp-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_USER_SIGNED_UP = "IsUserSignedUp";

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setIsUserSignedUp(boolean isUserSignedUp) {
        editor.putBoolean(IS_USER_SIGNED_UP, isUserSignedUp);
        editor.commit();
    }

    public boolean isUserSignedUp() {
        return pref.getBoolean(IS_USER_SIGNED_UP, false);
    }
}
