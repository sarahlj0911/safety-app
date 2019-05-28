package com.ASUPEACLab.safetyapp.walkthrough.landing;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.data.AppDatabase;
import com.ASUPEACLab.safetyapp.data.dao.WalkthroughDao;
import com.ASUPEACLab.safetyapp.data.entity.Walkthrough;

import java.util.concurrent.ExecutionException;


public class SaveNewWalkthrough extends AsyncTask<Void, Void, Walkthrough> {
    private static final String TAG = "SaveNewWalkthrough";
    private AppDatabase db;
    private Walkthrough walkthrough;
    private Long walkthroughId;
    private Walkthrough oldWalkthrough;
    private WalkthroughLandingContract.View view;

    public SaveNewWalkthrough(Walkthrough walkthrough, @Nullable Walkthrough oldWalkthrough, WalkthroughLandingContract.View view) {
        this.walkthrough = walkthrough;
        this.oldWalkthrough = oldWalkthrough;
        this.view = view;
    }

    @Override
    protected Walkthrough doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao dao = db.walkthroughDao();
        if (oldWalkthrough != null) {
            dao.insert(oldWalkthrough);
        }
        walkthroughId = dao.insert(walkthrough);
        walkthrough.setWalkthroughId(walkthroughId.intValue());
        return walkthrough;
    }

    @Override
    protected void onPostExecute(Walkthrough walkthrough) {
        try {
            this.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        view.openWalkthrough(walkthrough.getWalkthroughId(), walkthrough.getName());
    }
}
