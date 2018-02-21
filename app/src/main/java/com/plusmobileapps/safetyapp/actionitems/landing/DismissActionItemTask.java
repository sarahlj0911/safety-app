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

    public DismissActionItemTask(Response response) {
        this.response = response;
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
//        db.close();
    }
}
