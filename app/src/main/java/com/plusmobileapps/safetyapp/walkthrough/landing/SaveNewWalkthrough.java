package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;


public class SaveNewWalkthrough extends AsyncTask<Void, Void, Void> {

    private AppDatabase db;
    private Walkthrough walkthrough;
    private WalkthroughLandingContract.View view;

    public SaveNewWalkthrough(Walkthrough walkthrough, WalkthroughLandingContract.View view) {
        this.walkthrough = walkthrough;
        this.view = view;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao dao = db.walkthroughDao();
        dao.insert(walkthrough);
        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        view.openWalkthrough(walkthrough.getWalkthroughId(), walkthrough.getName());
    }
}
