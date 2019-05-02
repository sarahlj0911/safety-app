package com.ASUPEACLab.safetyapp.summary.detail;

import com.ASUPEACLab.safetyapp.BasePresenter;
import com.ASUPEACLab.safetyapp.BaseView;

/**
 * Created by Robert Beerman on 2/10/2018.
 */

public interface SummaryDetailsContract {

    interface View extends BaseView<Presenter> {
        void showTitle(String title);
    }

    interface Presenter extends BasePresenter {
        void loadDetails();

        void setTitle(String title);
    }

}
