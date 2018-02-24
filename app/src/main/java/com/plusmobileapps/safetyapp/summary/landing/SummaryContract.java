package com.plusmobileapps.safetyapp.summary.landing;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;
import com.plusmobileapps.safetyapp.walkthrough.landing.WalkthroughOverview;

import java.util.ArrayList;

public interface SummaryContract {

    interface View extends BaseView<Presenter> {
        void showSummaries(ArrayList<WalkthroughOverview> summaries);
        void showSummaryDetailUi(WalkthroughOverview summary);
    }

    interface Presenter extends BasePresenter {
        void loadSummaries(boolean forceUpdate);
        void openSummary(WalkthroughOverview requestedSummary);
    }
}