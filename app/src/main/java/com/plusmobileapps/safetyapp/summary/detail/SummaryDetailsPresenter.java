package com.plusmobileapps.safetyapp.summary.detail;

/**
 * Created by Robert Beerman on 2/10/2018.
 */

public class SummaryDetailsPresenter implements SummaryDetailsContract.Presenter {
    private static final String TAG = "SummaryDetailsPresenter";
    private SummaryDetailsContract.View summaryDetailsView;

    public SummaryDetailsPresenter(SummaryDetailsContract.View summaryDetailsView) {
        this.summaryDetailsView = summaryDetailsView;
        summaryDetailsView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
