package com.ASUPEACLab.safetyapp.summary.detail;

/**
 * Created by Robert Beerman on 2/10/2018.
 */

public class SummaryDetailsPresenter implements SummaryDetailsContract.Presenter {
    private static final String TAG = "SummaryDetailsPresenter";
    private SummaryDetailsContract.View view;

    SummaryDetailsPresenter(SummaryDetailsContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        // TODO Remove this call if not needed. It's just here for testing.
        setTitle("Testing Details Title");
    }

    public void setTitle(String title) {
        view.showTitle(title);
    }

    public void loadDetails() {

    }
}
