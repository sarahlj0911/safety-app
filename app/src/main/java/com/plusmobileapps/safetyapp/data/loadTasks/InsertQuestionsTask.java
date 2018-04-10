package com.plusmobileapps.safetyapp.data.loadTasks;


import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.entity.Question;

import java.util.List;

public class InsertQuestionsTask extends AsyncTask<Void, Void, Void> {

    private List<Question> questions;

    public InsertQuestionsTask(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        QuestionDao questionDao = AppDatabase.getAppDatabase(MyApplication.getAppContext()).questionDao();
        for (int i = 0; i < questions.size(); i++) {
            questionDao.insert(questions.get(i));
        }
        return null;
    }
}