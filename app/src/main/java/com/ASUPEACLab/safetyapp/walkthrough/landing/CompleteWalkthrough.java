package com.ASUPEACLab.safetyapp.walkthrough.landing;

import android.os.AsyncTask;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.data.AppDatabase;
import com.ASUPEACLab.safetyapp.data.dao.WalkthroughDao;
import com.ASUPEACLab.safetyapp.data.entity.Walkthrough;
import com.ASUPEACLab.safetyapp.sync.UploadTask;
import com.ASUPEACLab.safetyapp.util.DateTimeUtil;

/**
 * Created by aaronmusengo on 4/18/18.
 */

public class CompleteWalkthrough extends AsyncTask<Void, Void, Walkthrough> {
    private AppDatabase db;
    private Walkthrough walkthrough;
    private int walkthroughId;
    private WalkthroughLandingPresenter presenter;

    public CompleteWalkthrough(Walkthrough walkthrough, WalkthroughLandingPresenter presenter) {
        this.walkthrough = walkthrough;
        this.presenter = presenter;
        walkthrough.setPercentComplete(100);
        walkthrough.setLastUpdatedDate(DateTimeUtil.getDateTimeString());
    }

    @Override
    protected Walkthrough doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao dao = db.walkthroughDao();
        dao.insert(walkthrough);
        return walkthrough;
    }

    @Override
    protected void onPostExecute(Walkthrough walkthrough) {
        new LoadWalkthroughs(presenter.listener).execute();
        UploadTask uploadTask = new UploadTask(MyApplication.getAppContext());
        uploadTask.uploadData();
    }

}

