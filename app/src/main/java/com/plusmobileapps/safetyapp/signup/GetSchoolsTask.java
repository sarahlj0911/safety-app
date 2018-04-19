package com.plusmobileapps.safetyapp.signup;

import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.SchoolDao;
import com.plusmobileapps.safetyapp.data.entity.School;
import java.util.List;


public class GetSchoolsTask extends AsyncTask<Void, Void, List<School>> {

    private SignupPresenter.SignupLoadingListener listener;
    private static final String TAG = "GetSchoolsTask";
    private AppDatabase db;
    private List<School> schools;

    public GetSchoolsTask(SignupPresenter.SignupLoadingListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<School> doInBackground(Void... voids) {
        Log.d(TAG, "Loading Schools...");
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        SchoolDao dao = db.schoolDao();
        schools = dao.getAll();
        return schools;
    }

    protected void onPostExecute(List<School> schools) {
        Log.d(TAG, "Done loading schools.");
        listener.onSignupLoaded(schools);
    }
}
