package com.plusmobileapps.safetyapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.plusmobileapps.safetyapp.data.dao.*;
import com.plusmobileapps.safetyapp.data.entity.*;

@Database(entities = {Location.class, Question.class, Response.class, User.class, Walkthrough.class}, version = 1)
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
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database")
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
