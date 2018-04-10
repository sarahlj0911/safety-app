package com.plusmobileapps.safetyapp.actionitems.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;

import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.entity.Location;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.AppDatabase;

import java.util.List;

public class LoadActionItemTask extends AsyncTask<Void, Void, List<Response>> {
    private ActionItemContract.Presenter presenter;
    private AppDatabase db;
    private List<Response> actionItems;

    public LoadActionItemTask(ActionItemContract.Presenter presenter, List<Response> actionItems) {
        this.presenter = presenter;
        this.actionItems = actionItems;
    }

    @Override
    protected List<Response> doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao responseDao = db.responseDao();
        LocationDao locationDao = db.locationDao();
        QuestionDao questionDao = db.questionDao();

        List<Response> items = responseDao.getAllActionItems(1);

        for (Response actionItem : items) {
            int locationId = actionItem.getLocationId();
            Location location = locationDao.getByLocationId(locationId);
            actionItem.setLocationName(location.getName());

            int questionId = actionItem.getQuestionId();
            Question question = questionDao.getByQuestionID(questionId);
            String title = question.getShortDesc();
            actionItem.setTitle(title);

        }

        return items;
    }

    @Override
    protected void onPostExecute(List<Response> actionItems) {
        super.onPostExecute(actionItems);
        
        this.actionItems = actionItems;
        presenter.setActionItems(actionItems);
        presenter.loadActionItems(false);
    }
}
