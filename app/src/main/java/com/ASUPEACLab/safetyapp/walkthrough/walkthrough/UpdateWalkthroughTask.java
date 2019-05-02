package com.ASUPEACLab.safetyapp.walkthrough.walkthrough;

import android.os.AsyncTask;
import android.util.Log;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.data.AppDatabase;
import com.ASUPEACLab.safetyapp.data.dao.QuestionMappingDao;
import com.ASUPEACLab.safetyapp.data.dao.ResponseDao;
import com.ASUPEACLab.safetyapp.data.dao.WalkthroughDao;
import com.ASUPEACLab.safetyapp.data.entity.Walkthrough;
import com.ASUPEACLab.safetyapp.sync.UploadTask;
import com.ASUPEACLab.safetyapp.util.DateTimeUtil;

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
