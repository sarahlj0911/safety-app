package com.ASUPEACLab.safetyapp.signup;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.ASUPEACLab.safetyapp.BuildConfig;
import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.PrefManager;
import com.ASUPEACLab.safetyapp.data.entity.School;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;


public class GetSchoolsTask extends AsyncTask<Void, Void, GetSchoolsTask.Result> {

    private static final String TAG = "GetSchoolsTask";

    // Connection properties
    private static final String URL = BuildConfig.DB_URL;
    private static final String APP_ID = BuildConfig.APP_ID;
    private static final String PASS = BuildConfig.SYNC_PASS;

    private static final String SELECT_SCHOOLS_SQL = "SELECT DISTINCT schoolName, schoolId FROM schools";

    private Connection connection;
    private School school;
    private ArrayList<String> schools = new ArrayList<String>();
    private SignupDownloadCallback callback;
    private PrefManager prefManager;

    private SignupContract.View view;

    public GetSchoolsTask(SignupDownloadCallback callback) {
        setCallback(callback);
    }

    void setCallback(SignupDownloadCallback callback) {
        this.callback = callback;
    }

    static class Result {
        public String resultValue;
        public List<String> schoolList;
        public Exception exception;

        public Result(String resultValue) {
            this.resultValue = resultValue;
        }

        public Result(String resultValue, ArrayList<String> schoolList) {
            this.resultValue = resultValue;
            this.schoolList = schoolList;
        }

        public Result(Exception exception) {
            this.exception = exception;
        }
    }

    @Override
    protected void onPreExecute() {
        if (callback != null) {
            NetworkInfo networkInfo = callback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()
                    || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI)) {
                // If no connectivity, cancel task and update Callback with null data
                Log.d(TAG, "No connectivity. Cancelling download.");
                callback.updateFromDownload(null, null);
                cancel(true);
            } else {
                prefManager = new PrefManager(MyApplication.getAppContext());
                callback.updateFromDownload("Starting Schools download...", schools);
            }
        }
    }

    @Override
    protected GetSchoolsTask.Result doInBackground(Void... voids) {
        Result result = null;

        if (!isCancelled()) {
            Log.d(TAG, "Loading Schools...");
            try {
                result = new Result("Schools Downloaded", downloadSchoolList());
            } catch (Exception e) {
                result = new Result(e);
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Result result) {

        if (result != null && callback != null) {
            if (result.exception != null) {
                callback.updateFromDownload(result.exception.getMessage(), null);
            } else if (result.resultValue != null) {
                callback.updateFromDownload(result.resultValue, schools);
                Log.d(TAG, "Done loading schools.");
            }
        }

        callback.finishDownloading();
    }

    @Override
    protected void onCancelled(Result result) {

    }

    private ArrayList<String> downloadSchoolList() throws SQLException {
        PreparedStatement statement;
        ResultSet resultSet;
        connection = DriverManager.getConnection(URL, APP_ID, PASS);
        statement = connection.prepareStatement(SELECT_SCHOOLS_SQL);
        Log.d(TAG, "Looking up all schools in remote database...");
        resultSet = statement.executeQuery();

        while (resultSet.next()) {
            school = new School(0, null);
            school.setSchoolName(resultSet.getString("schoolName"));
            schools.add(school.getSchoolName());
        }

        closeDatabase(resultSet, statement, connection);
        return schools;
    }

    private void closeDatabase(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

}
