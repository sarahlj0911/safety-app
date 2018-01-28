package com.plusmobileapps.safetyapp.summary.landing;

import com.plusmobileapps.safetyapp.surveys.landing.SurveyOverview;

import java.util.ArrayList;

public interface SummaryContract {

    interface View {
        void showSummaries(ArrayList<SurveyOverview> summaries);
        void showSummaryDetailUi(SurveyOverview summary);
    }

    interface UserActionsListener {
        void loadSummaries(boolean forceUpdate);
        void openSummary(SurveyOverview requestedSummary);
    }
}