package com.plusmobileapps.safetyapp.summary.detail;


import android.content.Context;

/**
 * Created by Robert Beerman on 2/10/2018.
 */

public class SummaryOverviewPresenter implements SummaryOverviewContract.Presenter {
    private static final String TAG = "SummaryOverviewPresenter";
    private SummaryOverviewContract.View view;
    Context context;

    public SummaryOverviewPresenter(Context context) {
        this.context = context;
    }

    public SummaryOverviewPresenter(SummaryOverviewContract.View view, String walkthroughId) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        setTitle("Testing Overview Title");
    }

    public void loadWalkthroughs() {

    }

    public void setTitle(String title) {
        view.showTitle(title);
    }

    public void setFragment(SummaryOverviewFragment summaryOverviewFragment) {
        this.view = summaryOverviewFragment;
    }
}
