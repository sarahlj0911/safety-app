package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughLandingContract;
import com.plusmobileapps.safetyapp.walkthrough.walkthrough.question.WalkthroughFragmentContract;

import java.util.List;

/**
 * Created by asteinme on 3/8/18.
 */

public class GetCurrentWalkthroughIdTask extends AsyncTask<Void, Void, Integer> {


    private WalkthroughContract.Presenter presenter;

    public GetCurrentWalkthroughIdTask(WalkthroughContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao walkthroughDao = db.walkthroughDao();
        List<Walkthrough> walkthroughList = walkthroughDao.getAll();

        return walkthroughList.get(walkthroughList.size() - 1).getWalkthroughId();
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        presenter.loadResponses(i);
    }
}


