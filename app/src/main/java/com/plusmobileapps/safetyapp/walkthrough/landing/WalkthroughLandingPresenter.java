package com.plusmobileapps.safetyapp.walkthrough.landing;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import java.util.ArrayList;

import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import com.plusmobileapps.safetyapp.sync.DownloadCallback;
import com.plusmobileapps.safetyapp.sync.NetworkFragment;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WalkthroughLandingPresenter implements WalkthroughLandingContract.Presenter, DownloadCallback {

    private WalkthroughLandingContract.View view;
    private List<Walkthrough> walkthroughs;
    private Walkthrough walkthrough;
    private Context context;
    private NetworkFragment networkFragment;
    boolean downloading;

    //public WalkthroughLandingPresenter(WalkthroughLandingContract.View view) {
    public WalkthroughLandingPresenter(Context context, WalkthroughLandingContract.View view) {
        this.context = context;
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

    private WalkthroughListLoadingListener listener = new WalkthroughListLoadingListener() {
        @Override
        public void onWalkthroughListLoaded(List<Walkthrough> allWalkthroughs) {
            walkthroughs = allWalkthroughs;
            setupLandingUi();
        }
    };

    interface WalkthroughListLoadingListener {
        void onWalkthroughListLoaded(List<Walkthrough> allWalkthroughs);
    }

    // Below functions are for downloading remote walkthrough/response data for the entered school
    @Override
    public void updateFromDownload(String result) {
        downloading = true;
        view.showProgressBar(true);
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case DownloadCallback.Progress.ERROR:

                break;
            case DownloadCallback.Progress.CONNECT_SUCCESS:

                break;
            case DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS:

                break;
            case DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS:

                break;
        }
    }

    @Override
    public void finishDownloading() {
        downloading = false;
        view.showProgressBar(false);
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }
}
