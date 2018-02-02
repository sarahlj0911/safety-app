package com.plusmobileapps.safetyapp.surveys.landing;

import java.util.ArrayList;

public class SurveyLandingPresenter implements SurveyLandingContract.Presenter {

    private SurveyLandingContract.View view;
    private ArrayList<SurveyOverview> surveys;

    public SurveyLandingPresenter(SurveyLandingContract.View view) {
        this.view = view;
        view.setPresenter(this);
        surveys = new ArrayList<>(0);
    }

    @Override
    public void start() {
        loadSurveys();
    }

    @Override
    public void surveyClicked(int position) {
        view.openSurvey(surveys.get(position).getSurveyId());
    }

    @Override
    public void createNewSurveyConfirmed() {
        createNewSurvey();
    }

    @Override
    public void createNewSurveyClicked() {
        if(surveys.get(0).isInProgress()) {
            view.showConfirmationDialog();
        } else {
            createNewSurvey();
        }
    }

    private void createNewSurvey() {
        view.showCreateSurveyDialog();
    }

    @Override
    public void loadSurveys() {
        SurveyFakeModel model = new SurveyFakeModel();
        surveys = model.getSurveys();
        view.showSurveys(surveys);
    }

    @Override
    public void firstAppLaunch() {
        view.showTutorial();
    }
}
