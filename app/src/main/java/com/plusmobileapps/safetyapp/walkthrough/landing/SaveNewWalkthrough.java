package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.concurrent.ExecutionException;


public class SaveNewWalkthrough extends AsyncTask<Void, Void, Walkthrough> {
    private static final String TAG = "SaveNewWalkthrough";
    private AppDatabase db;
    private Walkthrough walkthrough;
    private Long walkthroughId;
    private WalkthroughLandingContract.View view;

    public SaveNewWalkthrough(Walkthrough walkthrough, WalkthroughLandingContract.View view) {
        this.walkthrough = walkthrough;
        this.view = view;
    }

    @Override
    protected Walkthrough doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao dao = db.walkthroughDao();
        walkthroughId = dao.insert(walkthrough);
        walkthrough.setWalkthroughId(walkthroughId.intValue());
        return walkthrough;
    }

    @Override
    protected void onPostExecute(Walkthrough walkthrough) {
        try {
            this.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        view.openWalkthrough(walkthrough.getWalkthroughId(), walkthrough.getName());
    }
}
