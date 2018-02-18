package com.plusmobileapps.safetyapp.actionitems.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;

import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.AppDatabase;

import java.util.List;

public class LoadActionItemTask extends AsyncTask<Void, Void, List<Response>> {
    ActionItemContract.View view;
    AppDatabase db;

    public LoadActionItemTask(ActionItemContract.View view) {
        this.view = view;
    }

    @Override
    protected List<Response> doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao responseDao = db.responseDao();
        List<Response> actionItems = responseDao.getAllActionItems();

        return actionItems;
    }

    @Override
    protected void onPostExecute(List<Response> actionItems) {
        super.onPostExecute(actionItems);
        view.showActionItems(actionItems);
        db.close();
    }
}
