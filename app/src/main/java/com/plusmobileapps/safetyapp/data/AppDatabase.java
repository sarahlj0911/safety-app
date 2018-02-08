package com.plusmobileapps.safetyapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


import com.plusmobileapps.safetyapp.data.dao.*;
import com.plusmobileapps.safetyapp.data.entity.*;
import com.huma.room_for_asset.RoomAsset;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.dao.UserDao;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Location;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.User;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

@Database(entities = {Location.class, Question.class, Response.class, School.class, User.class, Walkthrough.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract LocationDao locationDao();

    public abstract QuestionDao questionDao();

    public abstract ResponseDao responseDao();

    public abstract UserDao userDao();

    public abstract WalkthroughDao walkthroughDao();

    public static AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE =
                    RoomAsset.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "appDB.db")
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
