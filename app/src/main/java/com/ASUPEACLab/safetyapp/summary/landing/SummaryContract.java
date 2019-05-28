package com.ASUPEACLab.safetyapp.summary.landing;

import com.ASUPEACLab.safetyapp.BasePresenter;
import com.ASUPEACLab.safetyapp.BaseView;
import com.ASUPEACLab.safetyapp.data.entity.Walkthrough;

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