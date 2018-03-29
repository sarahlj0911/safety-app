package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.QuestionMappingDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.util.Date;
import java.util.List;

public class UpdateWalkthroughTask extends AsyncTask<Integer, Void, Boolean> {

    private static final String TAG = "UpdateWalkthruTask";
    private Integer walkthroughId;

    @Override
    protected Boolean doInBackground(Integer... walkthroughIds) {
        this.walkthroughId = walkthroughIds[0];
        AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao walkthroughDao = db.walkthroughDao();
        QuestionMappingDao questionMappingDao = db.questionMappingDao();
        ResponseDao responseDao = db.responseDao();
        Walkthrough walkthrough = walkthroughDao.getByWalkthroughId(walkthroughId.toString());

        // TODO Determine how many total questions there are for this walkthrough
        Double questionCount = ((Integer)questionMappingDao.getQuestionCount()).doubleValue();
        Log.d(TAG, "Question count: " + questionCount);
        // TODO Get count of responses for this walkthrough
        Double responseCount = ((Integer)responseDao.getResponseCount(walkthroughId)).doubleValue();
        Log.d(TAG, "Response count: " + responseCount);
        Double percentComplete = responseCount / questionCount;
        Log.d(TAG, "Walkthrough percent complete: " + percentComplete);

        Date date = new Date();
        String lastUpdatedDate = date.toString();
        walkthrough.setLastUpdatedDate(lastUpdatedDate);
        walkthrough.setPercentComplete(percentComplete);

        walkthroughDao.update(walkthrough);

        return true;
    }


}
