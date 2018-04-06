package com.plusmobileapps.safetyapp.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.dao.*;
import com.plusmobileapps.safetyapp.data.entity.*;
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
import com.plusmobileapps.safetyapp.data.loadTasks.InsertLocationsTask;
import com.plusmobileapps.safetyapp.data.loadTasks.InsertQuestionMappingTask;
import com.plusmobileapps.safetyapp.data.loadTasks.InsertQuestionsTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {Location.class, Question.class, QuestionMapping.class, Response.class, School.class, User.class, Walkthrough.class}, version = 2)
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
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "appDB.db")
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);

                                ArrayList<Integer> jsonFiles = new ArrayList<Integer>(){{
                                    add(R.raw.location);
                                    add(R.raw.question);
                                    add(R.raw.question_mapping);
                                }};

                                for (int i = 0; i < jsonFiles.size(); i++) {
                                    int rawResId = jsonFiles.get(i);
                                    //Reading source from local file
                                    InputStream inputStream = MyApplication.getAppContext().getResources().openRawResource(rawResId);
                                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                                    byte bufferByte[] = new byte[1024];
                                    int length;
                                    try {
                                        while ((length = inputStream.read(bufferByte)) != -1) {
                                            outputStream.write(bufferByte, 0, length);
                                        }
                                        outputStream.close();
                                        inputStream.close();
                                    } catch (IOException e) {

                                    }

                                    String json = outputStream.toString();
                                    Gson gson = new Gson();
                                    switch (rawResId) {
                                        case R.raw.location:
                                            Type locationType = new TypeToken<ArrayList<Location>>(){}.getType();
                                            List<Location> locations = gson.fromJson(json, locationType);
                                            new InsertLocationsTask(locations).execute();
                                            break;
                                        case R.raw.question:
                                            Type questionType = new TypeToken<ArrayList<Question>>(){}.getType();
                                            List<Question> questions = gson.fromJson(json, questionType);
                                            new InsertQuestionsTask(questions).execute();
                                            break;
                                        case R.raw.question_mapping:
                                            Type questionMappingType = new TypeToken<ArrayList<QuestionMapping>>(){}.getType();
                                            List<QuestionMapping> questionMappings = gson.fromJson(json, questionMappingType);
                                            new InsertQuestionMappingTask(questionMappings).execute();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        })
                        .build();

            }
            return INSTANCE;
        }
    }

    private Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            loadDatabase();
        }
    };

    private void loadDatabase() {

    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static class PopulateDbTask extends AsyncTask<Void, Void, Void> {



        public PopulateDbTask() {
        }

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }
    }

}
