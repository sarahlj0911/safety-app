package com.plusmobileapps.safetyapp.summary.landing;

import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughOverview;

import java.util.ArrayList;

public class SummaryPresenter implements SummaryContract.Presenter {

    private final SummaryContract.View summaryView;

    public SummaryPresenter(SummaryContract.View summaryView) {
        this.summaryView = summaryView;
        summaryView.setPresenter(this);
    }

    @Override
    public void start() {
        loadSummaries(false);
    }

    @Override
    public void loadSummaries(boolean forceUpdate) {
        FakeSummaryModel summaryModel = new FakeSummaryModel();
        ArrayList<WalkthroughOverview> summaries = summaryModel.getSummaries();
        summaryView.showSummaries(summaries);
    }

    @Override
    public void openSummary(WalkthroughOverview requestedSummary) {
        summaryView.showSummaryDetailUi(requestedSummary);
    }
}
