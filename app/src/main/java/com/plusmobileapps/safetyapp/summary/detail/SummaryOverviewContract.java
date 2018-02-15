package com.plusmobileapps.safetyapp.summary.detail;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;

/**
 * Created by Robert Beerman on 2/10/2018.
 */

public interface SummaryOverviewContract {

    interface View extends BaseView<Presenter> {
        void showTitle(String title);
    }

    interface Presenter extends BasePresenter {
        void loadWalkthroughs();
        void setTitle(String title);
    }
}
