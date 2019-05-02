package com.ASUPEACLab.safetyapp.actionitems.detail;

import android.os.AsyncTask;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.data.AppDatabase;
import com.ASUPEACLab.safetyapp.data.dao.ResponseDao;
import com.ASUPEACLab.safetyapp.data.entity.Response;

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
        dao.insert(response);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        view.finishActivity();
    }
}