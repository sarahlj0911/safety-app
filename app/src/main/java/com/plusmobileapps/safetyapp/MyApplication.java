package com.plusmobileapps.safetyapp;

import android.app.Application;
import android.content.Context;

import com.plusmobileapps.safetyapp.data.entity.Location;
import com.plusmobileapps.safetyapp.data.singletons.Locations;
import com.plusmobileapps.safetyapp.data.singletons.QuestionMappings;
import com.plusmobileapps.safetyapp.data.singletons.Questions;
import com.plusmobileapps.safetyapp.data.singletons.Responses;
import com.plusmobileapps.safetyapp.data.singletons.Schools;
import com.plusmobileapps.safetyapp.data.singletons.Users;
import com.plusmobileapps.safetyapp.data.singletons.Walkthroughs;

/**
 * Created by Andrew on 1/17/2018.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        loadSingletonData();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    private void loadSingletonData() {
        Locations.getInstance();
        QuestionMappings.getInstance();
        Questions.getInstance();
        Responses.getInstance();
        Schools.getInstance();
        Users.getInstance();
        Walkthroughs.getInstance();

    }

}
