package com.ASUPEACLab.safetyapp.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.R;
import com.ASUPEACLab.safetyapp.data.dao.*;
import com.ASUPEACLab.safetyapp.data.entity.*;
import com.ASUPEACLab.safetyapp.data.dao.LocationDao;
import com.ASUPEACLab.safetyapp.data.dao.QuestionDao;
import com.ASUPEACLab.safetyapp.data.dao.ResponseDao;
import com.ASUPEACLab.safetyapp.data.dao.UserDao;
import com.ASUPEACLab.safetyapp.data.dao.WalkthroughDao;
import com.ASUPEACLab.safetyapp.data.entity.Location;
import com.ASUPEACLab.safetyapp.data.entity.Question;
import com.ASUPEACLab.safetyapp.data.entity.Response;
import com.ASUPEACLab.safetyapp.data.entity.User;
import com.ASUPEACLab.safetyapp.data.entity.Walkthrough;
import com.ASUPEACLab.safetyapp.data.loadTasks.InsertLocationsTask;
import com.ASUPEACLab.safetyapp.data.loadTasks.InsertQuestionMappingTask;
import com.ASUPEACLab.safetyapp.data.loadTasks.InsertQuestionsTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {Location.class, Question.class, QuestionMapping.class, Response.class, School.class, User.class, Walkthrough.class}, version = 4)
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

                                ArrayList<Integer> jsonFiles = new ArrayList<Integer>() {{
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
                                            Type locationType = new TypeToken<ArrayList<Location>>() {
                                            }.getType();
                                            List<Location> locations = gson.fromJson(json, locationType);
                                            new InsertLocationsTask(locations).execute();
                                            break;
                                        case R.raw.question:
                                            Type questionType = new TypeToken<ArrayList<Question>>() {
                                            }.getType();
                                            List<Question> questions = gson.fromJson(json, questionType);
                                            new InsertQuestionsTask(questions).execute();
                                            break;
                                        case R.raw.question_mapping:
                                            Type questionMappingType = new TypeToken<ArrayList<QuestionMapping>>() {
                                            }.getType();
                                            List<QuestionMapping> questionMappings = gson.fromJson(json, questionMappingType);
                                            new InsertQuestionMappingTask(questionMappings).execute();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        })
                        .addMigrations(MIGRATION_2_3).allowMainThreadQueries()
                        .build();

            }
            return INSTANCE;
        }
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create new responses table with new primary key constraints
            database.execSQL("CREATE TABLE responses_tmp (responseId INTEGER NOT NULL, isActionItem INTEGER NOT NULL, image TEXT, " +
                    "locationId INTEGER NOT NULL, timestamp TEXT, rating INTEGER NOT NULL, priority INTEGER NOT NULL, " +
                    "actionPlan TEXT, questionId INTEGER NOT NULL, userId INTEGER NOT NULL, walkthroughId INTEGER NOT NULL, " +
                    "PRIMARY KEY(responseId, walkthroughId, locationId, questionId), " +
                    "FOREIGN KEY(walkthroughId) REFERENCES walkthroughs(walkthroughId) ON UPDATE NO ACTION ON DELETE CASCADE, " +
                    "FOREIGN KEY(userId) REFERENCES user(userId) ON UPDATE NO ACTION ON DELETE NO ACTION, " +
                    "FOREIGN KEY(locationId) REFERENCES location(locationId) ON UPDATE NO ACTION ON DELETE NO ACTION, " +
                    "FOREIGN KEY(questionId) REFERENCES question(questionId) ON UPDATE NO ACTION ON DELETE NO ACTION)");

            // Copy old data into the new responses table
            database.execSQL("INSERT INTO responses_tmp (responseId, isActionItem, image, locationId, timestamp, rating, priority, " +
                    "actionPlan, questionId, userId, walkthroughId) " +
                    "SELECT responseId, isActionItem, image, locationId, timestamp, rating, priority, actionPlan, " +
                    "questionId, userId, walkthroughId FROM responses");

            // Drop the old responses table
            database.execSQL("DROP TABLE responses");

            // Rename the new responses table to the old name
            database.execSQL("ALTER TABLE responses_tmp RENAME TO responses");
        }
    };
}
