package com.plusmobileapps.safetyapp.summary.landing;

import java.util.ArrayList;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

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
        ArrayList<Walkthrough> summaries = summaryModel.getSummaries();
        summaryView.showSummaries(summaries);
    }

    @Override
    public void openSummary(Walkthrough requestedSummary) {
        summaryView.showSummaryDetailUi(requestedSummary);
    }
}
