package com.plusmobileapps.safetyapp.summary.landing;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.ArrayList;

public interface SummaryContract {

    interface View extends BaseView<Presenter> {
        void showSummaries(ArrayList<Walkthrough> summaries);
        void showSummaryDetailUi(Walkthrough summary);
    }

    interface Presenter extends BasePresenter {
        void loadSummaries(boolean forceUpdate);
        void openSummary(Walkthrough requestedSummary);
    }
}