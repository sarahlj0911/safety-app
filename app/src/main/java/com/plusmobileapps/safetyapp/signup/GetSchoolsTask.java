package com.plusmobileapps.safetyapp.signup;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.BuildConfig;
import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.SchoolDao;
import com.plusmobileapps.safetyapp.data.entity.School;
import com.plusmobileapps.safetyapp.sync.DownloadCallback;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import java.sql.Connection;


public class GetSchoolsTask extends AsyncTask<Void, Void, GetSchoolsTask.Result> {

    private SignupPresenter.SignupLoadingListener listener;
    private static final String TAG = "GetSchoolsTask";

    // Connection properties
    private static final String URL = BuildConfig.DB_URL;
    private static final String APP_ID = BuildConfig.APP_ID;
    private static final String PASS = BuildConfig.SYNC_PASS;

    private static final String SELECT_SCHOOLS_SQL = "SELECT DISTINCT schoolName, schoolId FROM schools";

    private Connection connection;
    private AppDatabase db;
    private School school;
    private List<School> schools;
    private DownloadCallback callback;
    private PrefManager prefManager;

    public GetSchoolsTask(DownloadCallback callback) {
        setCallback(callback);
    }

    void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }

    public GetSchoolsTask(SignupPresenter.SignupLoadingListener listener) {
        this.listener = listener;
    }

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

    @Override
    protected void onPreExecute() {
        if (callback != null) {
            NetworkInfo networkInfo = callback.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected()
                    || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI)) {
                // If no connectivity, cancel task and update Callback with null data
                Log.d(TAG, "No connectivity. Cancelling download.");
                callback.updateFromDownload(null);
                cancel(true);
            } else {
                prefManager = new PrefManager(MyApplication.getAppContext());
                callback.updateFromDownload("Starting download...");
            }
        }
    }

    @Override
    protected GetSchoolsTask.Result doInBackground(Void... voids) {
        Result result = null;

        if(!isCancelled()) {
            Log.d(TAG, "Loading Schools...");
            try {
                getSchoolList();
                result = new Result("Schools Downloaded");
            } catch(Exception e) {
                result = new Result(e);
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Result result) {

        if(result != null && callback != null) {
            if(result.exception != null) {
                callback.updateFromDownload(result.exception.getMessage());
            } else if(result.resultValue != null) {
                callback.updateFromDownload(result.resultValue);
                Log.d(TAG, "Done loading schools.");
            }
        }

        callback.finishDownloading();
    }

    @Override
    protected void onCancelled(Result result) {

    }

    private void getSchoolList() throws SQLException {
        PreparedStatement statement;
        ResultSet resultSet;

        connection = DriverManager.getConnection(URL, APP_ID, PASS);
        statement = connection.prepareStatement(SELECT_SCHOOLS_SQL);
        Log.d(TAG, "Looking up all schools in remote database...");
        resultSet = statement.executeQuery();

        while(resultSet.next()) {
            school = new School(0, null);
            school.setSchoolId(resultSet.getInt("schooolId"));
            school.setSchoolName(resultSet.getString("schoolName"));
            schools.add(school);
        }

        for(School s: schools) {
            Log.d(TAG, school.getSchoolName() + " " + school.getSchoolId());
        }
    }

    private void closeDatabase(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if(resultSet != null) {
                resultSet.close();
            }
            if(statement != null) {
                statement.close();
            }
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }
}
