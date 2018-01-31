package com.plusmobileapps.safetyapp.surveys.landing;

import java.util.ArrayList;

public interface SurveyLandingContract {

    interface View {
        void showSurveys(ArrayList<SurveyOverview> surveys);
        void openSurvey(long id);
        void showTutorial();
        void showConfirmationDialog();
        void showCreateSurveyDialog();
    }

    interface UserActionsListener {
        void surveyClicked(int position);
        void createNewSurveyClicked();
        void firstAppLaunch();
        void loadSurveys();
        void createNewSurveyConfirmed();
    }
}
