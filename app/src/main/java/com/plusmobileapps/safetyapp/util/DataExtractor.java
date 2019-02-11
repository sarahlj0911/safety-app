package com.plusmobileapps.safetyapp.util;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemPresenter;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;

public class DataExtractor {
    private AppDatabase db;
    //ActionItemPresenter presenter = new ActionItemPresenter();

    public String getStrings(){
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());

        String toReturn="";
        ResponseDao responseDao = db.responseDao();
        LocationDao locationDao = db.locationDao();
        QuestionDao questionDao = db.questionDao();


        //find where walkthrough data is stored return it as a string.


        return toReturn;

    }
}
