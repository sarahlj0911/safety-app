package com.plusmobileapps.safetyapp.summary.detail;


/**
 * Created by Robert Beerman on 2/10/2018.
 */

public class SummaryOverviewPresenter implements SummaryOverviewContract.Presenter {
    private static final String TAG = "SummaryOverviewPresenter";
    private SummaryOverviewContract.View view;

    public SummaryOverviewPresenter(SummaryOverviewContract.View view, String walkthroughId) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
