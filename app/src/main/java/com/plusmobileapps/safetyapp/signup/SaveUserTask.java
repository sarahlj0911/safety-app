package com.plusmobileapps.safetyapp.signup;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.UserDao;
import com.plusmobileapps.safetyapp.data.entity.User;

/**
 * Created by Robert Beerman on 2/19/2018.
 */

public class SaveUserTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "SaveUserTask";
    private AppDatabase db;
    private User user;

    SaveUserTask(User user) {
        this.user = user;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        UserDao dao = db.userDao();
        dao.insert(user);

        return true;
    }

    @Override
    protected void onPostExecute(Boolean finished) {
        super.onPostExecute(finished);
        db.close();
    }
}
