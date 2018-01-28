package com.plusmobileapps.safetyapp.summary.landing;

import com.plusmobileapps.safetyapp.surveys.landing.SurveyOverview;

import java.util.ArrayList;

public class FakeSummaryModel {

    private ArrayList<SurveyOverview> surveys;

    public FakeSummaryModel() {
        surveys = new ArrayList<>();
        surveys = new ArrayList<>();
        SurveyOverview survey2 = new SurveyOverview("Fall 2017");
        survey2.setDate("Oct. 4, 2017\n1:30 p.m.");

        SurveyOverview survey3 = new SurveyOverview("Summer 2017");
        survey3.setDate("Jul. 9, 2017\n9:00 a.m.");

        SurveyOverview survey1 = new SurveyOverview("Spring 2017");
        survey1.setDate("Mar. 3, 2017\n11:30 a.m.");

        surveys.add(survey2);
        surveys.add(survey3);
        surveys.add(survey1);
    }

    public ArrayList<SurveyOverview> getSummaries(){
        return surveys;
    }
}
