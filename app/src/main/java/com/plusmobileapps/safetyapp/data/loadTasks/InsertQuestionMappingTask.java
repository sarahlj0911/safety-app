package com.plusmobileapps.safetyapp.data.loadTasks;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.QuestionMappingDao;
import com.plusmobileapps.safetyapp.data.entity.QuestionMapping;

import java.util.List;

public class InsertQuestionMappingTask extends AsyncTask<Void, Void, Void> {

    private List<QuestionMapping> questionMappings;

    public InsertQuestionMappingTask(List<QuestionMapping> questionMappings) {
        this.questionMappings = questionMappings;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        QuestionMappingDao questionMappingDao = AppDatabase.getAppDatabase(MyApplication.getAppContext()).questionMappingDao();
        for (int i = 0; i < questionMappings.size(); i++) {
            questionMappingDao.insert(questionMappings.get(i));
        }
        return null;
    }
}
