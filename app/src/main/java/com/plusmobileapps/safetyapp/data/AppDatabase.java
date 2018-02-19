package com.plusmobileapps.safetyapp.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.VisibleForTesting;


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

@Database(entities = {Location.class, Question.class, QuestionMapping.class, Response.class, School.class, User.class, Walkthrough.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract LocationDao locationDao();

    public abstract QuestionDao questionDao();

    public abstract ResponseDao responseDao();

    public abstract UserDao userDao();

    public abstract WalkthroughDao walkthroughDao();

    public abstract SchoolDao schoolDao();

    public abstract QuestionMappingDao questionMappingDao();

    private static final Object sLock = new Object();

    public static AppDatabase getAppDatabase(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE =
                        RoomAsset.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "appDB.db")
                                .build();
            }
            return INSTANCE;
        }
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /*@VisibleForTesting
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE user ADD COLUMN userName TEXT");
        }
    };*/
}
