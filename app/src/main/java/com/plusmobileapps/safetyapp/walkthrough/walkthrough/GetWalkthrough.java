package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

/**
 * Created by kneil on 3/19/2018.
 */

public class GetWalkthrough extends AsyncTask<Void, Void, Walkthrough> {
    private AppDatabase db;
    Walkthrough walkthrough;
    private WalkthroughContract.Presenter presenter;
    private String id;

    public GetWalkthrough(WalkthroughContract.Presenter presenter, String id) {
        this.presenter = presenter;
        this.id = id;
    }


    protected Walkthrough doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao walkthroughDao = db.walkthroughDao();
        walkthrough = walkthroughDao.getByWalkthroughId(id);
        return walkthrough;
    }

    protected void onPostExecute(Walkthrough walkthrough) {
        presenter.setWalkthrough(walkthrough);
    }
}
