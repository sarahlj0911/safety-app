package com.plusmobileapps.safetyapp.actionitems.detail;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Response;

public class LoadActionItemDetailTask extends AsyncTask<Void, Void, Response> {

    private AppDatabase db;
    private String actionItemId;
    private ActionItemDetailContract.View view;
    private ActionItemDetailPresenter.ResponseLoadingListener listener;



    public LoadActionItemDetailTask(String id, ActionItemDetailPresenter.ResponseLoadingListener listener) {
        actionItemId = id;
        this.listener = listener;
    }

    @Override
    protected Response doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao dao = db.responseDao();
        Response response = dao.getByResponseId(actionItemId);
        //TODO: verify that response if not null
        //return response;
        return new Response(0,1,1,"11:34pm",3, 2, "Fix it", 2, "",1, 1);
    }

    @Override
    protected void onPostExecute(Response response) {
        listener.onResponseLoaded(response);
        db.close();
    }
}