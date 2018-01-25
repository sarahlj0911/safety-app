package com.plusmobileapps.safetyapp.summary.landing;

import com.plusmobileapps.safetyapp.surveys.landing.SurveyOverview;

import java.util.ArrayList;

public class SummaryPresenter implements SummaryContract.UserActionsListener {

    private final SummaryContract.View summaryView;

    public SummaryPresenter(SummaryContract.View summaryView) {
        this.summaryView = summaryView;
    }

    @Override
    public void loadSummaries(boolean forceUpdate) {
        FakeSummaryModel summaryModel = new FakeSummaryModel();
        ArrayList<SurveyOverview> summaries = summaryModel.getSummaries();
        summaryView.showSummaries(summaries);
    }

    @Override
    public void openSummary(SurveyOverview requestedSummary) {
        summaryView.showSummaryDetailUi(requestedSummary);
    }
}
