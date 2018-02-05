package com.plusmobileapps.safetyapp.walkthrough.landing;

import java.util.ArrayList;

public class SurveyFakeModel {

    private ArrayList<SurveyOverview> surveys;

    public SurveyFakeModel() {
        surveys = new ArrayList<>(0);
        populateData();
    }

    public ArrayList<SurveyOverview> getSurveys() {
        return surveys;
    }

    /**
     * Create mock data
     */
    private void populateData() {

        //SurveyOverview survey1 = new SurveyOverview("Spring 2017");
        SurveyOverview survey2 = new SurveyOverview("Fall 2017", "Sep 3, 2017", "4:33 p.m.");
        survey2.setSurveyId(0L);
        SurveyOverview survey3 = new SurveyOverview("Summer 2017", "Sep 3, 2017", "4:33 p.m.");
        survey3.setSurveyId(1L);

        SurveyOverview survey1 = new SurveyOverview("Fall 2017", "Dec 12, 2017", "3:33 p.m.");
        survey1.setSurveyId(2L);
        survey1.setProgress(50);
        surveys.add(survey1);
        surveys.add(survey2);
        surveys.add(survey3);
    }





}
