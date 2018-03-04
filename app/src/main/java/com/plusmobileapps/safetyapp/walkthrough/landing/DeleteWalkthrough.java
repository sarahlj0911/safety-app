package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;


public class DeleteWalkthrough extends AsyncTask<Void, Void, Void> {

    private AppDatabase db;
    private Walkthrough walkthrough;
    private WalkthroughLandingContract.View view;
    private int walkthroughId;

    public DeleteWalkthrough(Walkthrough walkthrough, WalkthroughLandingContract.View view) {
        this.walkthrough = walkthrough;
        this.view = view;

        this.walkthrough.setIsDeleted(1);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao dao = db.walkthroughDao();
        //dao.delete(walkthrough);
        dao.logicalDelete(walkthrough);
        return null;
    }
}
