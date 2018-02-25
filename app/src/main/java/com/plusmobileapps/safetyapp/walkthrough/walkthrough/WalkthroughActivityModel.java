package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.entity.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronmusengo on 2/23/18.
 */

public class WalkthroughActivityModel extends AsyncTask<Void, Void, List<Question>> {

    private List<Question> questions = new ArrayList<>();
    private WalkthroughContract.View view;
    private WalkthroughPresenter presenter;
    AppDatabase db;
    private int locationId;

    public WalkthroughActivityModel(int locationId, WalkthroughContract.View view, WalkthroughPresenter presenter) {
        this.locationId = locationId;
        this.view = view;
        this.presenter = presenter;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    protected List<Question> doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        QuestionDao questionDao = db.questionDao();
        questions = questionDao.getQuestionsForLocationType(this.locationId);
        return questions;
    }

    @Override
    protected void onPostExecute(List<Question> questions) {
        super.onPostExecute(questions);
        presenter.setQuestions(questions);
        view.showNextQuestion(questions.get(0));
    }
}

