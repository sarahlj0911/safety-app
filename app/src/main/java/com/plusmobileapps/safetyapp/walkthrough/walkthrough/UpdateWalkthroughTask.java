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

import java.util.Date;
import java.util.List;

import static android.content.Context.ACCOUNT_SERVICE;

public class UpdateWalkthroughTask extends AsyncTask<Integer, Void, Boolean> {

    private static final String TAG = "UpdateWalkthruTask";
    private Integer walkthroughId;

    // SyncAdapter Constants
    // The authority for the sync adapter's content provier
    public static final String AUTHORITY = "com.plusmobileapps.safetyapp.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "safetyapp.com";
    // The account name
    public static final String ACCOUNT = "default_account";
    // Instance fields
    Account account;
    ContentResolver mResolver;
    Context context;

    public UpdateWalkthroughTask(WalkthroughContract.View view){
        context = ((AppCompatActivity) view).getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        account = CreateSyncAccount(context);
        mResolver = context.getContentResolver();
    }

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
        Double questionCount = ((Integer)questionMappingDao.getQuestionCount()).doubleValue();
        Log.d(TAG, "Question count: " + questionCount);
        // Get count of responses for this walkthrough
        Double responseCount = ((Integer)responseDao.getResponseCount(walkthroughId)).doubleValue();
        Log.d(TAG, "Response count: " + responseCount);
        Double percentComplete = responseCount / questionCount * 100.0;
        Log.d(TAG, "Walkthrough percent complete: " + percentComplete);

        Date date = new Date();
        String lastUpdatedDate = date.toString();
        walkthrough.setLastUpdatedDate(lastUpdatedDate);
        walkthrough.setPercentComplete(percentComplete);

        walkthroughDao.update(walkthrough);

        return true;
    }

    @Override
    protected void onPostExecute(Boolean saved){
        if (saved) {
            uploadData();
        }
    }

    public void uploadData() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        Log.d(TAG, "Requesting sync");
        ContentResolver.requestSync(account, AUTHORITY, settingsBundle);
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1) here.
             */
        } else {
            Log.d(TAG, "Account already exists or some other error occurred.");
        }

        Log.d(TAG, "Sync account created!");

        return newAccount;
    }
}
