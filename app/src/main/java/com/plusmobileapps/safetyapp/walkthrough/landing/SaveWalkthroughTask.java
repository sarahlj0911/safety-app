package com.plusmobileapps.safetyapp.walkthrough.landing;

import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

/**
 * Created by asteinme on 4/23/18.
 */

public class SaveWalkthroughTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = SaveWalkthroughTask.class.getName();

    private Walkthrough walkthrough;

    public SaveWalkthroughTask(Walkthrough walkthrough) {
        this.walkthrough = walkthrough;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao dao = db.walkthroughDao();
        dao.insert(walkthrough);
        Log.d(TAG, "Walkthrough was saved!");
        return null;
    }
}
