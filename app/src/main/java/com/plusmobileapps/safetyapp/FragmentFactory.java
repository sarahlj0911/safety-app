package com.plusmobileapps.safetyapp;

import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemsFragment;
import com.plusmobileapps.safetyapp.summary.landing.SummaryFragment;
import com.plusmobileapps.safetyapp.walkthrough.landing.SurveyLandingFragment;

public class FragmentFactory {
    private static FragmentFactory instance = null;
    private SurveyLandingFragment surveyLandingFragment;
    private ActionItemsFragment actionItemsFragment;
    private SummaryFragment summaryFragment;

    private FragmentFactory() {
        surveyLandingFragment = SurveyLandingFragment.newInstance();
        actionItemsFragment = ActionItemsFragment.newInstance();
        summaryFragment = SummaryFragment.newInstance();
    }

    public static FragmentFactory getInstance(){
        if(instance == null) {
            instance = new FragmentFactory();
        }

        return instance;
    }

    public SurveyLandingFragment getSurveyLandingFragment() {
        return surveyLandingFragment;
    }

    public ActionItemsFragment getActionItemsFragment() {
        return actionItemsFragment;
    }

    public SummaryFragment getSummaryFragment() {
        return summaryFragment;
    }

}