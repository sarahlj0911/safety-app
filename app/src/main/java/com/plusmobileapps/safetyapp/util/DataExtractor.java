package com.plusmobileapps.safetyapp.util;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemPresenter;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Location;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.List;
import java.util.Stack;

public class DataExtractor {

    private AppDatabase db;
    private String value;
    private int numberVal;
    private Stack info = new Stack();
    //ActionItemPresenter presenter = new ActionItemPresenter();
    public DataExtractor(String inputValue,int i){
        this.value = inputValue;
        this.numberVal = i;


    }
    public DataExtractor(){
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());


        ResponseDao responseDao = db.responseDao();
        LocationDao locationDao = db.locationDao();
        QuestionDao questionDao = db.questionDao();
        List<Response> items = responseDao.getAllActionItems(1);
        String [] titleStrings;


        for (Response actionItem : items) {
            int locationId = actionItem.getLocationId();
            Location location = locationDao.getByLocationId(locationId);
            actionItem.setLocationName(location.getName());

            info.push(actionItem.getTitle().toString());
            int questionId = actionItem.getQuestionId();

            Question question = questionDao.getByQuestionID(questionId);
            info.push(question.getShortDesc().toString());

            String title = question.getShortDesc();
            actionItem.setTitle(title);
            info.push(title.toString());

        }

    }

    public void addItem(String value) {
        info.push(value);
    }
    public void removeitem(){
        info.pop();
    }
    public String getItem(){
        return info.pop().toString();
    }

    public String peekItem(){

        return info.peek().toString();
    }

    public Stack<Response> getlist(){
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());


        ResponseDao responseDao = db.responseDao();
        LocationDao locationDao = db.locationDao();
        QuestionDao questionDao = db.questionDao();
        List<Response> items = responseDao.getAllActionItems(1);
        String [] titleStrings;


        for (Response actionItem : items) {
            int locationId = actionItem.getLocationId();
            Location location = locationDao.getByLocationId(locationId);
            actionItem.setLocationName(location.getName());

            info.push(actionItem.getTitle().toString());
            int questionId = actionItem.getQuestionId();

            Question question = questionDao.getByQuestionID(questionId);
            info.push(question.getShortDesc().toString());

            String title = question.getShortDesc();
            actionItem.setTitle(title);
            info.push(title.toString());

        }


        //find where walkthrough data is stored return it as a string.


        return info;

    }
}
