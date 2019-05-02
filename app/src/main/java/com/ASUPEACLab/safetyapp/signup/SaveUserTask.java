package com.ASUPEACLab.safetyapp.signup;

import android.os.AsyncTask;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.data.AppDatabase;
import com.ASUPEACLab.safetyapp.data.dao.UserDao;
import com.ASUPEACLab.safetyapp.data.entity.User;

/**
 * Created by Robert Beerman on 2/19/2018.
 */

public class SaveUserTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "SaveUserTask";
    private AppDatabase db;
    private User user;

    public SaveUserTask(User user) {
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

    }
}
