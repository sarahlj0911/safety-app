package com.plusmobileapps.safetyapp.walkthrough.landing;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.ArrayList;
import java.util.List;

public interface WalkthroughLandingContract {

    interface View extends BaseView<Presenter>{
        void showWalkthroughs(List<Walkthrough> walkthroughs);
        void openWalkthrough(long id, String title);
        void createNewWalkthrough(String title);
        void showTutorial();
        void showInProcessConfirmationDialog();
        void showCreateWalkthroughDialog();
    }

    interface Presenter extends BasePresenter {
        void walkthroughClicked(int position);
        void createNewWalkthroughIconClicked();
        void firstAppLaunch();
        void deleteInProgressWalkthroughConfirmed();
        void confirmCreateWalkthroughClicked(String title);
    }
}
