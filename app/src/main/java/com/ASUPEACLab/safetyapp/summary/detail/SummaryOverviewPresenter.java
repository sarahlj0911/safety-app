package com.ASUPEACLab.safetyapp.summary.detail;

/**
 * Created by Robert Beerman on 2/10/2018.
 */

public class SummaryOverviewPresenter implements SummaryOverviewContract.Presenter {
    private static final String TAG = "SummaryOverviewPresenter";
    private SummaryOverviewContract.View view;

    SummaryOverviewPresenter(SummaryOverviewContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        // TODO Remove this call if not needed. It's just here for testing.
        setTitle("Testing Overview Title");
    }

    public void loadWalkthroughs() {

    }

    public void setTitle(String title) {
        view.showTitle(title);
    }
}
