package com.plusmobileapps.safetyapp.sync;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.BuildConfig;
import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.dao.SchoolDao;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.School;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Robert Beerman on 4/5/2018.
 */

public class DownloadTask extends AsyncTask<Void, Integer, DownloadTask.Result> {

    private static final String TAG = "DownloadTask";

    // Connection properties
    private static final String URL = BuildConfig.DB_URL;
    private static final String APP_ID = BuildConfig.APP_ID;
    private static final String PASS = BuildConfig.SYNC_PASS;

    private static final String GET_WALKTHROUGHS_AND_RESPONSES_SQL =
            "select w.walkthroughId AS WALKTHROUGH_ID, w.userId AS WALKTHROUGH_USER, " +
            "w.name AS NAME, w.lastUpdatedDate AS LAST_UPDATED_DATE, w.createdDate AS CREATED_DATE, " +
            "w.percentComplete AS PERCENT_COMPLETE, r.userId AS RESPONSE_USER, " +
            "r.locationId AS LOCATION_ID, r.questionId AS QUESTION_ID, r.actionPlan AS ACTION_PLAN, " +
            "r.priority AS PRIORITY, r.rating AS RATING, r.timestamp AS TIMESTAMP, " +
            "r.isActionItem AS IS_ACTION_ITEM, r.image AS IMAGE_PATH " +
            "from safetywalkthrough.walkthroughs w " +
            "join safetywalkthrough.responses r on w.schoolId = r.schoolId and w.walkthroughId = r.walkthroughId " +
            "where w.schoolId = ?";

    private Connection conn;
    private AppDatabase db;
    private SchoolDao schoolDao;
    private WalkthroughDao walkthroughDao;
    private ResponseDao responseDao;
    private DownloadCallback callback;
    private School school;

    public DownloadTask(DownloadCallback callback) {
        setCallback(callback);
    }

    void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }

    /**
     * Wrapper class that serves as a union of a result value and an exception. When the download
     * task has completed, either the result value or exception can be a non-null value.
     * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
     */
    static class Result {
        public String resultValue;
        public Exception exception;

        public Result(String resultValue) {
            this.resultValue = resultValue;
        }

        public Result(Exception exception) {
            this.exception = exception;
        }
    }

    /**
     * Cancel background network operation if we do not have network connectivity
     */
    @Override
    protected void onPreExecute() {
        if (callback != null) {
            NetworkInfo networkInfo = callback.getActiveNetworkInfo();
            Log.d(TAG, networkInfo.toString() + ", isConnected: " + networkInfo.isConnected());
            if (networkInfo == null || !networkInfo.isConnected()
                    || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI)) {
                // If no connectivity, cancel task and update Callback with null data
                Log.d(TAG, "No connectivity. Cancelling download.");
                callback.updateFromDownload(null);
                cancel(true);
            }
        }
    }

    /**
     * Defines work to perform on the background thread. This is where the magic happens!
     */
    @Override
    protected DownloadTask.Result doInBackground(Void... voids) {
        Result result = null;
        if (!isCancelled()) {
            db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
            schoolDao = db.schoolDao();
            walkthroughDao = db.walkthroughDao();
            responseDao = db.responseDao();

            int schoolId = schoolDao.get().getRemoteId();

            try {
                getWalkthroughsAndResponses(schoolId);
                result = new Result("Success!");
            } catch(Exception e) {
                result = new Result(e);
            }

        }

        return result;
    }

    private void getWalkthroughsAndResponses(int schoolId) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        Set<Walkthrough> walkthroughs = new HashSet<>();
        List<Response> responses = new ArrayList<>();

        conn = DriverManager.getConnection(URL, APP_ID, PASS);
        stmt = conn.prepareStatement(GET_WALKTHROUGHS_AND_RESPONSES_SQL);
        stmt.setInt(1, schoolId);
        rs = stmt.executeQuery();

        while (rs.next()) {
            Walkthrough walkthrough = new Walkthrough(rs.getString("NAME"));
            walkthrough.setWalkthroughId(rs.getInt("WALKTHROUGH_ID"));
            walkthrough.setLastUpdatedDate(rs.getTimestamp("LAST_UPDATED_DATE").toString());
            walkthrough.setCreatedDate(rs.getTimestamp("CREATED_DATE").toString());
            walkthrough.setPercentComplete(rs.getFloat("PERCENT_COMPLETE"));

            walkthroughs.add(walkthrough);

            Response response = new Response();
            response.setUserId(rs.getInt("RESPONSE_USER"));
            response.setLocationId(rs.getInt("LOCATION_ID"));
            response.setQuestionId(rs.getInt("QUESTION_ID"));
            response.setActionPlan(rs.getString("ACTION_PLAN"));
            response.setPriority(rs.getInt("PRIORITY"));
            response.setRating(rs.getInt("RATING"));
            response.setTimeStamp(rs.getString("TIMESTAMP"));
            response.setIsActionItem(rs.getInt("IS_ACTION_ITEM"));
            response.setImagePath(rs.getString("IMAGE_PATH"));

            responses.add(response);

        }

        for (Walkthrough w : walkthroughs) {
            Log.d(TAG, w.toString());
        }

        for (Response r : responses) {
            Log.d(TAG, r.toString());
        }

        cleanup(rs, stmt, conn);
    }

    private void cleanup(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Updates the DownloadCallback with the result
     */
    @Override
    protected void onPostExecute(Result result) {

        if (result != null && callback != null) {

            if (result.exception != null) {

                callback.updateFromDownload(result.exception.getMessage());
            } else if (result.resultValue != null) {

                callback.updateFromDownload(result.resultValue);
            }
        }

        callback.finishDownloading();
    }

    /**
     * Override to add special behavior for cancelled AsyncTask
     */
    @Override
    protected void onCancelled(Result result) {

    }
}
