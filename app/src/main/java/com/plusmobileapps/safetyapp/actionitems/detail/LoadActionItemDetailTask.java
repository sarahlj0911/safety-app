package com.plusmobileapps.safetyapp.actionitems.detail;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;

public class LoadActionItemDetailTask extends AsyncTask<Void, Void, Response> {

    private AppDatabase db;
    private String actionItemId;
    private ActionItemDetailPresenter.ResponseLoadingListener listener;

    public LoadActionItemDetailTask(String id, ActionItemDetailPresenter.ResponseLoadingListener listener) {
        actionItemId = id;
        this.listener = listener;
    }

    @Override
    protected Response doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao responseDao = db.responseDao();
        LocationDao locationDao = db.locationDao();
        QuestionDao questionDao = db.questionDao();


        Response response = responseDao.getByResponseId(actionItemId);
        String locationName = locationDao.getByLocationId(response.getLocationId()).getName();
        response.setLocationName(locationName);

        Question question = questionDao.getByQuestionID(response.getQuestionId());
        String title = question.getShortDesc();
        response.setTitle(title);

        int rating = response.getRating();
        String ratingText = "";

        if (rating == 1) {
            ratingText = question.getRatingOption1();
        } else if (rating == 2) {
            ratingText = question.getRatingOption2();
        } else if (rating == 3) {
            ratingText = question.getRatingOption3();
        } else if (rating == 4) {
            ratingText = question.getRatingOption4();
        }

        response.setRatingText(ratingText);

        return response;
    }

    @Override
    protected void onPostExecute(Response response) {
        listener.onResponseLoaded(response);
    }
}