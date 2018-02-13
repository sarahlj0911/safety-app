package com.plusmobileapps.safetyapp.walkthrough.landing;

import java.util.ArrayList;

public class WalkthroughLandingPresenter implements WalkthroughLandingContract.Presenter {

    private WalkthroughLandingContract.View view;
    private ArrayList<WalkthroughOverview> walkthroughs;

    public WalkthroughLandingPresenter(WalkthroughLandingContract.View view) {
        this.view = view;
        view.setPresenter(this);
        walkthroughs = new ArrayList<>(0);
    }

    @Override
    public void start() {
        loadWalkthroughs();
    }

    @Override
    public void walkthroughClicked(int position) {
        final WalkthroughOverview walkthrough = walkthroughs.get(position);
        view.openWalkthrough(walkthrough.getWalkthroughId(), walkthrough.getTitle());
    }

    @Override
    public void createNewWalkthroughConfirmed() {
        createNewWalkthrough();
    }

    @Override
    public void createNewWalkthroughClicked() {
        if(walkthroughs.get(0).isInProgress()) {
            view.showConfirmationDialog();
        } else {
            createNewWalkthrough();
        }
    }

    private void createNewWalkthrough() {
        view.showCreateWalkthroughDialog();
    }

    @Override
    public void loadWalkthroughs() {
        WalkthroughFakeModel model = new WalkthroughFakeModel();
        walkthroughs = model.getWalkthroughs();
        view.showWalkthroughs(walkthroughs);
    }

    @Override
    public void confirmCreateWalkthroughClicked(String title) {
        view.createNewWalkthrough(title);
    }

    @Override
    public void firstAppLaunch() {
        view.showTutorial();
    }
}
