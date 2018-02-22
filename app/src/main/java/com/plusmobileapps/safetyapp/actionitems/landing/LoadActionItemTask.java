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
//        actionItems = responseDao.getAllActionItems();
        //List<Question> questions = questionDao.getAll();
        Response response = new Response(0,
                1,
                1,
                "11:34pm",
                3,
                2,
                "Fix it",
                2,
                "",
                1,
                1);
        actionItems.add(response);

        return actionItems;
    }

    @Override
    protected void onPostExecute(List<Response> actionItems) {
        super.onPostExecute(actionItems);
//        view.showActionItems(actionItems);
        List<Response> items = new ArrayList<>(0);
        items.add(new Response(0,1,1,"11:34pm",3, 2, "Fix it", 2, "",1,1));
        view.showActionItems(items);
    }
}
