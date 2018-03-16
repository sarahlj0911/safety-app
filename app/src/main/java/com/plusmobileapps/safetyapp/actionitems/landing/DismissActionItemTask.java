package com.plusmobileapps.safetyapp.actionitems.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.actionitems.detail.ActionItemDetailContract;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Response;

/**
 * Created by asteinme on 2/21/18.
 */

public class DismissActionItemTask extends AsyncTask<Void, Void, Void> {
    private AppDatabase db;
    private Response response;
    private ActionItemContract.Presenter presenter;

    public DismissActionItemTask(Response response, ActionItemContract.Presenter presenter) {
        this.response = response;
        this.presenter = presenter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao dao = db.responseDao();
        dao.insert(response);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        presenter.loadActionItems(false);
    }
}
