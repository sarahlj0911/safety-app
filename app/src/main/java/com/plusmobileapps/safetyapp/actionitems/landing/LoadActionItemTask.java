package com.plusmobileapps.safetyapp.actionitems.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.ActionItem;
import com.plusmobileapps.safetyapp.data.ActionItemDao;
import com.plusmobileapps.safetyapp.data.AppDatabase;

import java.util.List;

public class LoadActionItemTask extends AsyncTask<Void, Void, List<ActionItem>> {
    ActionItemContract.View view;
    AppDatabase db;

    public LoadActionItemTask(ActionItemContract.View view) {
        this.view = view;
    }

    @Override
    protected List<ActionItem> doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ActionItemDao actionItemDao = db.actionItemDao();
        return actionItemDao.getAll();
    }

    @Override
    protected void onPostExecute(List<ActionItem> actionItems) {
        super.onPostExecute(actionItems);
        view.showActionItems(actionItems);
        db.close();
    }
}
