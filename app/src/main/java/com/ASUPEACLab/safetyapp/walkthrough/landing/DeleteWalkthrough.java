package com.ASUPEACLab.safetyapp.walkthrough.landing;

import android.os.AsyncTask;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.data.AppDatabase;
import com.ASUPEACLab.safetyapp.data.dao.WalkthroughDao;
import com.ASUPEACLab.safetyapp.data.entity.Walkthrough;


public class DeleteWalkthrough extends AsyncTask<Void, Void, Void> {

    private AppDatabase db;
    private Walkthrough walkthrough;
    private WalkthroughLandingContract.View view;
    private int walkthroughId;

    public DeleteWalkthrough(Walkthrough walkthrough, WalkthroughLandingContract.View view) {
        this.walkthrough = walkthrough;
        this.view = view;

        // Perform logical delete
        this.walkthrough.setIsDeleted(1);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao dao = db.walkthroughDao();
        //dao.delete(walkthrough);
        dao.update(walkthrough);
        return null;
    }
}
