package com.plusmobileapps.safetyapp.walkthrough.walkthrough;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.arch.persistence.room.Update;
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.QuestionMappingDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import com.plusmobileapps.safetyapp.sync.UploadTask;
import com.plusmobileapps.safetyapp.util.DateTimeUtil;

import java.util.Date;
import java.util.List;

import static android.content.Context.ACCOUNT_SERVICE;

public class UpdateWalkthroughTask extends AsyncTask<Integer, Void, Boolean> {

    private static final String TAG = "UpdateWalkthruTask";
    private Integer walkthroughId;

    //public UpdateWalkthroughTask(WalkthroughContract.View view) {
    //context = ((AppCompatActivity) view).getApplicationContext();
    //}


    @Override
    protected Boolean doInBackground(Integer... walkthroughIds) {
        this.walkthroughId = walkthroughIds[0];

        AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        WalkthroughDao walkthroughDao = db.walkthroughDao();
        QuestionMappingDao questionMappingDao = db.questionMappingDao();
        ResponseDao responseDao = db.responseDao();
        Walkthrough walkthrough = walkthroughDao.getByWalkthroughId(walkthroughId.toString());

        if (walkthrough.getPercentComplete() == 100) {
            return true;
        }
        // Determine how many total questions there are for this walkthrough
        Double questionCount = ((Integer) questionMappingDao.getQuestionCount()).doubleValue();
        Log.d(TAG, "Question count: " + questionCount);
        // Get count of responses for this walkthrough
        Double responseCount = ((Integer) responseDao.getResponseCount(walkthroughId)).doubleValue();
        Log.d(TAG, "Response count: " + responseCount);
        Double percentComplete = responseCount / questionCount * 100.0;
        Log.d(TAG, "Walkthrough percent complete: " + percentComplete);

        walkthrough.setLastUpdatedDate(DateTimeUtil.getDateTimeString());
        walkthrough.setPercentComplete(percentComplete);

        walkthroughDao.update(walkthrough);

        return true;
    }

    @Override
    protected void onPostExecute(Boolean saved) {
        if (saved) {
            UploadTask uploadTask = new UploadTask(MyApplication.getAppContext());
            uploadTask.uploadData();
        }
    }
}
