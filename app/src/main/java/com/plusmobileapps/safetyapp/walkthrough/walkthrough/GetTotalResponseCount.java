package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;

/**
 * Created by kneil on 3/19/2018.
 */

public class GetTotalResponseCount extends AsyncTask<Void, Void, Integer> {
    private AppDatabase db;
    private ResponseDao responseDao;
    private WalkthroughContract.Presenter presenter;
    int totalResponses;

    public GetTotalResponseCount(WalkthroughContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        responseDao = db.responseDao();
        totalResponses = responseDao.getAll().size();
        return totalResponses;
    }

    @Override
    protected void onPostExecute(Integer result) {
        presenter.setTotalResponseCount(result);
        presenter.calculateProgress();
    }
}
