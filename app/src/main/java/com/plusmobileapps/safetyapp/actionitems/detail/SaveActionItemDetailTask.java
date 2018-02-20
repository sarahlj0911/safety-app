package com.plusmobileapps.safetyapp.actionitems.detail;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Response;

public class SaveActionItemDetailTask extends AsyncTask<Void, Void, Void> {

    private AppDatabase db;
    private Response response;
    private ActionItemDetailContract.View view;

    public SaveActionItemDetailTask(Response response, ActionItemDetailContract.View view) {
        this.response = response;
        this.view = view;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao dao = db.responseDao();
        //TODO: uncomment once the dao insert statement is working
//        dao.insertAll(response);
        //TODO: Verify that the response edits were saved
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        db.close();
        view.finishActivity();
    }
}