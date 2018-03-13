package com.plusmobileapps.safetyapp.walkthrough.landing;


import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.data.entity.Walkthrough;


import java.util.ArrayList;

import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WalkthroughLandingPresenter implements WalkthroughLandingContract.Presenter {

    private WalkthroughLandingContract.View view;
    private List<Walkthrough> walkthroughs;
    private Walkthrough walkthrough;

    public WalkthroughLandingPresenter(WalkthroughLandingContract.View view) {
        this.view = view;
        view.setPresenter(this);
        walkthroughs = new ArrayList<>(0);
    }

    @Override
    public void start() {
        new LoadWalkthroughs(listener).execute();
    }

    @Override
    public void walkthroughClicked(int position) {
        final Walkthrough walkthrough = walkthroughs.get(position);
        view.openWalkthrough(walkthrough.getWalkthroughId(), walkthrough.getName());
    }

    @Override
    public void deleteInProgressWalkthroughConfirmed() {
        new DeleteWalkthrough(walkthroughs.get(0), view).execute();
        createNewWalkthrough();
    }

    @Override
    public void createNewWalkthroughIconClicked() {
        if(walkthroughs.size() > 0) {
            if(walkthroughs.get(0).isInProgress()) {
                view.showInProcessConfirmationDialog();
            } else {
                createNewWalkthrough();
            }
        } else {
            createNewWalkthrough();
        }
    }

    private void createNewWalkthrough() {
        view.showCreateWalkthroughDialog();

    }


    private void setupLandingUi(List<Walkthrough> walkthroughs) {
        view.showWalkthroughs(walkthroughs);
    }

    @Override
    public void confirmCreateWalkthroughClicked(String title) {
        walkthrough = new Walkthrough(title);

        new SaveNewWalkthrough(walkthrough, view).execute();
    }


    @Override
    public void firstAppLaunch() {
        view.showTutorial();
    }

    private WalkthroughListLoadingListener listener = new WalkthroughListLoadingListener() {
        @Override
        public void onWalkthroughListLoaded(List<Walkthrough> allWalkthroughs) {
            walkthroughs = allWalkthroughs;
            setupLandingUi(walkthroughs);

        }
    };

    interface WalkthroughListLoadingListener {
        void onWalkthroughListLoaded(List<Walkthrough> allWalkthroughs);
    }
}
