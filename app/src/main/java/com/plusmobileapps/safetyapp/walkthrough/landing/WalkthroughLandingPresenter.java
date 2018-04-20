package com.plusmobileapps.safetyapp.walkthrough.landing;

import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import java.util.ArrayList;
import java.util.List;

public class WalkthroughLandingPresenter implements WalkthroughLandingContract.Presenter {

    private static final String TAG = "WalkthruLandPresenter";
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
        final Walkthrough walkthrough = walkthroughs.get(walkthroughs.size() - position - 1);
        view.openWalkthrough(walkthrough.getWalkthroughId(), walkthrough.getName());
    }

    @Override
    public void deleteInProgressWalkthroughConfirmed() {
        new DeleteWalkthrough(walkthroughs.get(walkthroughs.size() - 1), view).execute();
        createNewWalkthrough();
    }

    @Override
    public void createNewWalkthroughIconClicked() {
        if(walkthroughs.size() > 0) {
            if(walkthroughs.get(walkthroughs.size() - 1).isInProgress()) {
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

    @Override
    public void setWalkthroughCompleted(Walkthrough selectedWalkthrough, WalkthroughLandingContract.Presenter presenter) {
        new CompleteWalkthrough(selectedWalkthrough, this).execute();
    }

    private void setupLandingUi() {
        view.showNoWalkThrough(walkthroughs.size() == 0);
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

    public WalkthroughListLoadingListener listener = new WalkthroughListLoadingListener() {
        @Override
        public void onWalkthroughListLoaded(List<Walkthrough> allWalkthroughs) {
            walkthroughs = allWalkthroughs;
            setupLandingUi();
        }
    };

    interface WalkthroughListLoadingListener {
        void onWalkthroughListLoaded(List<Walkthrough> allWalkthroughs);
    }
}
