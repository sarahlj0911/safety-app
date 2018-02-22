package com.plusmobileapps.safetyapp.data.singletons;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionMappingDao;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.QuestionMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronmusengo on 2/18/18.
 */

public class Questions {
    private static final Questions ourInstance = new Questions();
    private AppDatabase db;
    private QuestionDao questionDao;
    private QuestionMappingDao questionMappingDao;

    private List<Question> questionList;
    private List<QuestionMapping> questionMappingList;

    public static Questions getInstance() {
        return ourInstance;
    }

    private Questions() {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        questionDao = db.questionDao();
        questionMappingDao = db.questionMappingDao();

        populateQuestions();
        populateMapping();
    }

    private void populateQuestions() {
        questionList = questionDao.getAll();
    }

    private void populateMapping() {
        questionMappingList = questionMappingDao.getAllMappings();
    }

    //public methods
    public List<Question> getAllQuestions() {
        return this.questionList;
    }

    public List<Question> getAllQuestionsForLocation(int locationId) {
        List<Integer> questionIds = new ArrayList<Integer>();
        List<Question> returnList = new ArrayList<Question>();

        for (int i = 0; i < questionMappingList.size(); i++) {
            QuestionMapping tempMapping = questionMappingList.get(i);

            if (tempMapping.getLocationId() == locationId) {
                questionIds.add(tempMapping.getQuestionId());
            }
        }

        for (int i = 0; i < questionList.size(); i++) {
            Question tempQuestion = questionList.get(i);

            if (questionIds.contains(tempQuestion.getQuestionId())) {
                returnList.add(tempQuestion);
            }
        }

        return returnList;
    }

    public Question getQuestionWithId(int questionId) {
        for (int i = 0; i < questionList.size(); i++) {
            Question tempQuestion = questionList.get(i);
            if (tempQuestion.getQuestionId() == questionId) {
                return tempQuestion;
            }
        }

        return null;
    }

    public void addQuestion(Question question, int locationId) {
        int questionId = question.getQuestionId();

    }
}
