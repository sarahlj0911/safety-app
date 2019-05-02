package com.ASUPEACLab.safetyapp.signup;

import android.os.AsyncTask;
import android.util.Log;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.data.AppDatabase;
import com.ASUPEACLab.safetyapp.data.dao.SchoolDao;
import com.ASUPEACLab.safetyapp.data.entity.School;

/**
 * Created by Robert Beerman on 2/19/2018.
 */

public class SaveSchoolTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "SaveSchoolTask";
    private AppDatabase db;
    private School school;

    public SaveSchoolTask(School school) {
        this.school = school;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        SchoolDao dao = db.schoolDao();
        dao.insert(school);

        return true;
    }

    @Override
    protected void onPostExecute(Boolean finished) {
        Log.d(TAG,"school saved");

    }
}
