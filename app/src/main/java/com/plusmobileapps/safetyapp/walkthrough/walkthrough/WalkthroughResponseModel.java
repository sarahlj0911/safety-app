package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronmusengo on 2/25/18.
 */

public class WalkthroughResponseModel extends AsyncTask<Void, Void, List<Response>> {

    private List<Response> responses = new ArrayList<>();
    private WalkthroughContract.View view;
    private WalkthroughPresenter presenter;

    private AppDatabase db;
    private int locationId;
    private int walkthroughId;

    public WalkthroughResponseModel(int locationId, int walkthroughId, WalkthroughContract.View view, WalkthroughPresenter presenter) {
        this.locationId = locationId;
        this.walkthroughId = walkthroughId;
        this.view = view;
        this.presenter = presenter;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setWalkthroughId(int walkthroughId) {
        this.walkthroughId = walkthroughId;
    }

    @Override
    protected List<Response> doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao responseDao = db.responseDao();
        responses = responseDao.getResponsesForLocation(locationId, walkthroughId);
        return responses;
    }

    @Override
    protected void onPostExecute(List<Response> responses) {
        super.onPostExecute(responses);
        presenter.setResponses(responses);
    }

}

