package com.plusmobileapps.safetyapp.walkthrough.landing;

import com.plusmobileapps.safetyapp.BasePresenter;
import com.plusmobileapps.safetyapp.BaseView;

import java.util.ArrayList;

public interface WalkthroughLandingContract {

    interface View extends BaseView<Presenter>{
        void showWalkthroughs(ArrayList<WalkthroughOverview> walkthroughs);
        void openWalkthrough(long id, String title);
        void createNewWalkthrough(String title);
        void showTutorial();
        void showConfirmationDialog();
        void showCreateWalkthroughDialog();
    }

    interface Presenter extends BasePresenter {
        void walkthroughClicked(int position);
        void createNewWalkthroughClicked();
        void firstAppLaunch();
        void loadWalkthroughs();
        void createNewWalkthroughConfirmed();
        void confirmCreateWalkthroughClicked(String title);
    }
}
