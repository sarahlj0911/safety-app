package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.os.AsyncTask;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.ArrayList;

/**
 * Created by kneil on 2/21/2018.
 */

public class LoadWalkthroughs extends AsyncTask<Void, Void, ArrayList<com.plusmobileapps.safetyapp.data.entity.Walkthrough>> {

    private AppDatabase db;
    private ArrayList<Walkthrough> walkthroughs;
    private WalkthroughLandingContract.View view;
    private WalkthroughLandingPresenter.WalkthroughListLoadingListener listener;

    public LoadWalkthroughs(WalkthroughLandingPresenter.WalkthroughListLoadingListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Walkthrough> doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase((MyApplication.getAppContext()));
        WalkthroughDao dao = db.walkthroughDao();
        walkthroughs = (ArrayList)dao.getAll();
        return walkthroughs;
    }

    protected void onPostExecute(ArrayList<com.plusmobileapps.safetyapp.data.entity.Walkthrough> walkthroughs) {
        listener.onWalkthroughListLoaded(walkthroughs);
    }
}
