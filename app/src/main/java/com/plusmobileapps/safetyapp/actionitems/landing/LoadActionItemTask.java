package com.plusmobileapps.safetyapp.actionitems.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.ActionItem;
import com.plusmobileapps.safetyapp.data.ActionItemDao;
import com.plusmobileapps.safetyapp.data.AppDatabase;

import java.util.List;

/**
 * Android Documentation
 * https://developer.android.com/reference/android/os/AsyncTask.html
 *
 *
 * Void - Params, the type of the parameters sent to the task upon execution.
 *          (change to whatever object you want to pass into the task)
 * Void - Progress, the type of the progress units published during the background computation.
 *          (Int if you are actually displaying a progress bar)
 * List<ActionItem> - Result, the type of the result of the background computation.
 */
public class LoadActionItemTask extends AsyncTask<Void, Void, List<ActionItem>> {
    ActionItemContract.View view;
    AppDatabase db;

    public LoadActionItemTask(ActionItemContract.View view) {
        this.view = view;
    }

    /**
     * invoked on the UI thread before the task is executed. This step is normally used to setup
     * the task, for instance by showing a progress bar in the user interface.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        view.setProgressIndicator(true);
    }

    /**
     * invoked on the background thread immediately after onPreExecute() finishes executing.
     *
     * @param voids didn't need to pass anything in since it is just a query of all action items
     *                 (This would usually be Params)
     * @return  List<ActionItem> contains all action items
     */
    @Override
    protected List<ActionItem> doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ActionItemDao actionItemDao = db.actionItemDao();
        return actionItemDao.getAll();
    }

    /**
     * invoked on the UI thread after the background computation finishes
     *
     * @param actionItems   list of action items from the db query
     */
    @Override
    protected void onPostExecute(List<ActionItem> actionItems) {
        super.onPostExecute(actionItems);
        view.setProgressIndicator(false);
        view.showActionItems(actionItems);
        db.close();
    }
}
