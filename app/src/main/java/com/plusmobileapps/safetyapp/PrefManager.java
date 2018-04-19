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

    private static final String IS_TUTORIAL_SEEN = "IsTutorialSeen";
    private static final String IS_USER_SIGNED_UP = "IsUserSignedUp";
    private static final String HAS_SEEN_CREATE_WALKTHROUGH_TUTORIAL = "HasSeenCreateWalkthroughTutorial";
    private static final String LAST_RESPONSE_ID = "LastResponseId";

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsTutorialSeen(boolean tutorialSeen) {
        editor.putBoolean(IS_TUTORIAL_SEEN, tutorialSeen);
        editor.commit();
    }

    public boolean isTutorialSeen() {
        return pref.getBoolean(IS_TUTORIAL_SEEN, false);
    }

    public void setIsUserSignedUp(boolean isUserSignedUp) {
        editor.putBoolean(IS_USER_SIGNED_UP, isUserSignedUp);
        editor.commit();
    }

    public boolean isUserSignedUp() {
        return pref.getBoolean(IS_USER_SIGNED_UP, false);
    }

    public void setUserSeenCreateWalkthroughTutorial(boolean seen) {
        editor.putBoolean(HAS_SEEN_CREATE_WALKTHROUGH_TUTORIAL, seen);
        editor.commit();
    }

    public boolean getHasSeenCreateWalkthroughTutorial() {
        return pref.getBoolean(HAS_SEEN_CREATE_WALKTHROUGH_TUTORIAL, false);
    }

    public void setLastResponseUniqueId(int lastId) {
        editor.putInt(LAST_RESPONSE_ID, lastId);
        editor.commit();
    }

    public int getLastResponseUniqueId() {
        return pref.getInt(LAST_RESPONSE_ID, 0);
    }
}
