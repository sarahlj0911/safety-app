package com.plusmobileapps.safetyapp.actionitems.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;

import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoadActionItemTask extends AsyncTask<Void, Void, List<Response>> {
    private ActionItemContract.View view;
    private AppDatabase db;
    private List<Response> actionItems;

    public LoadActionItemTask(ActionItemContract.View view, List<Response> actionItems) {
        this.view = view;
        this.actionItems = actionItems;
    }

    @Override
    protected List<Response> doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao responseDao = db.responseDao();
        return responseDao.getAllActionItems();
    }

    @Override
    protected void onPostExecute(List<Response> actionItems) {
        super.onPostExecute(actionItems);
        List<Response> filteredList = new ArrayList<>(0);
        for (Response response : actionItems) {
            if(response.isActionItem()) {
                filteredList.add(response);
            }
        }

        this.actionItems.addAll(filteredList);
        view.showActionItems(filteredList);
    }
}
