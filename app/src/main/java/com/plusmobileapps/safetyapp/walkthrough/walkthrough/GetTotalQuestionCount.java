package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.content.Intent;
import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;

/**
 * Created by kneil on 3/19/2018.
 */

public class GetTotalQuestionCount extends AsyncTask<Void, Void, Integer> {
    private AppDatabase db;
    private QuestionDao questionDao;
    private WalkthroughContract.Presenter presenter;
    int totalQuestions;

    public GetTotalQuestionCount(WalkthroughContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        questionDao = db.questionDao();
        totalQuestions = questionDao.getAll().size();
        return totalQuestions;
    }

    @Override
    protected void onPostExecute(Integer result) {
        presenter.setTotalQuestionCount(result);
    }
}
