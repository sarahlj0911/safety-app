package com.plusmobileapps.safetyapp.walkthrough.landing;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;

import java.util.ArrayList;

public interface SurveyLandingContract {

    interface View extends BaseView<Presenter>{
        void showSurveys(ArrayList<SurveyOverview> surveys);
        void openSurvey(long id);
        void showTutorial();
        void showConfirmationDialog();
        void showCreateSurveyDialog();
    }

    interface Presenter extends BasePresenter {
        void surveyClicked(int position);
        void createNewSurveyClicked();
        void firstAppLaunch();
        void loadSurveys();
        void createNewSurveyConfirmed();
    }
}
