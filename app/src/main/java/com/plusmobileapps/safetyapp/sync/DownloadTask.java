package com.plusmobileapps.safetyapp.sync;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.plusmobileapps.safetyapp.BuildConfig;
import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.dao.SchoolDao;
import com.plusmobileapps.safetyapp.data.dao.UserDao;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.data.entity.School;
import com.plusmobileapps.safetyapp.data.entity.User;
import com.plusmobileapps.safetyapp.data.entity.Walkthrough;
import com.plusmobileapps.safetyapp.util.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

    private static final String SELECT_SCHOOL_SQL = "SELECT schoolId FROM schools WHERE schoolName = ?";
    public static final String SELECT_MAX_SCHOOL_ID_SQL = "SELECT MAX(schoolId) FROM schools";
    public static final String INSERT_SCHOOL_SQL = "INSERT INTO schools VALUES (?, ?)";
    public static final String SELECT_USER_ID_FROM_USER_SQL = "SELECT userId FROM user WHERE emailAddress = ?";
    public static final String SELECT_MAX_USER_ID_SQL = "SELECT MAX(userId) FROM user";
    public static final String INSERT_USER_SQL = "INSERT INTO user (userId, schoolId, userName, " +
            "emailAddress, role) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_WALKTHROUGHS_AND_RESPONSES_SQL =
            "select w.walkthroughId AS WALKTHROUGH_ID, w.userId AS WALKTHROUGH_USER, " +
            "w.name AS NAME, w.lastUpdatedDate AS LAST_UPDATED_DATE, w.createdDate AS CREATED_DATE, " +
            "w.percentComplete AS PERCENT_COMPLETE, " +
            "r.responseId AS RESPONSE_ID, r.locationId AS LOCATION_ID, r.questionId AS QUESTION_ID, " +
            "r.actionPlan AS ACTION_PLAN, r.priority AS PRIORITY, r.rating AS RATING, r.timestamp AS TIMESTAMP, " +
            "r.isActionItem AS IS_ACTION_ITEM, r.image AS IMAGE_PATH " +
            "from safetywalkthrough.walkthroughs w " +
            "left outer join safetywalkthrough.responses r on w.schoolId = r.schoolId and w.walkthroughId = r.walkthroughId " +
            "where w.schoolId = ?";

    private Connection conn;
    private AppDatabase db;
    private SchoolDao schoolDao;
    private UserDao userDao;
    private WalkthroughDao walkthroughDao;
    private ResponseDao responseDao;
    private DownloadCallback callback;
    private School school;
    private User user = null;
    private PrefManager prefManager;

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

    /**
     * Defines work to perform on the background thread. This is where the magic happens!
     */
    @Override
    protected DownloadTask.Result doInBackground(Void... voids) {
        Result result = null;
        int remoteSchoolId;
        int remoteUserId;

        if (!isCancelled()) {


            db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
            schoolDao = db.schoolDao();
            userDao = db.userDao();
            walkthroughDao = db.walkthroughDao();
            responseDao = db.responseDao();

            school = schoolDao.get();

            try {
                if (!school.isRegistered()) {
                    Log.d(TAG, "School not registered");
                    remoteSchoolId = registerSchool(school);
                    school.setRemoteId(remoteSchoolId);
                    schoolDao.insert(school);
                    Log.d(TAG, "Registered school remote id = " + remoteSchoolId);
                } else {
                    remoteSchoolId = school.getRemoteId();
                    Log.d(TAG, "School " + school.getSchoolName() + " is already registered");
                }

                user = userDao.getUser();
                if (!user.isRegistered()) {
                    Log.d(TAG, "User not registered");
                    remoteUserId = registerUser(user, remoteSchoolId);
                    user.setRemoteId(remoteUserId);
                    userDao.insert(user);
                    Log.i(TAG, "Registered user remote id = " + remoteUserId);
                }

                getWalkthroughsAndResponses(remoteSchoolId);
                result = new Result("Success!");
            } catch(Exception e) {
                result = new Result(e);
            }

        }

        return result;
    }

    private int registerSchool(School school) throws SQLException {
        String schoolName = school.getSchoolName();
        int remoteId = 0;
        List<Statement> statements = new ArrayList<>();
        List<ResultSet> resultSets = new ArrayList<>();

        PreparedStatement selectSchoolStmt;
        Statement getNextSchoolIdStmt;
        PreparedStatement insertSchoolStmt;
        ResultSet rs;
        ResultSet nextSchoolIdRs;

        conn = DriverManager.getConnection(URL, APP_ID, PASS);
        conn.setAutoCommit(false);
        Log.i(TAG, "Successfully connected to database");


        selectSchoolStmt = conn.prepareStatement(SELECT_SCHOOL_SQL);
        statements.add(selectSchoolStmt);
        selectSchoolStmt.setString(1, schoolName);

        rs = selectSchoolStmt.executeQuery();
        resultSets.add(rs);

        if (!rs.next() || rs.getInt(1) == 0) {
            getNextSchoolIdStmt = conn.createStatement();
            statements.add(getNextSchoolIdStmt);
            nextSchoolIdRs = getNextSchoolIdStmt.executeQuery(SELECT_MAX_SCHOOL_ID_SQL);
            resultSets.add(nextSchoolIdRs);

            if (!nextSchoolIdRs.next()) {
                remoteId = 1;
            } else {
                do {
                    remoteId = nextSchoolIdRs.getInt(1) + 1;
                } while (nextSchoolIdRs.next());
            }

            Log.d(TAG, "Inserting school with values [" + remoteId + ", " + schoolName +"]");
            String insertSchoolSql = INSERT_SCHOOL_SQL;
            insertSchoolStmt = conn.prepareStatement(insertSchoolSql);
            statements.add(insertSchoolStmt);
            insertSchoolStmt.setInt(1, remoteId);
            insertSchoolStmt.setString(2, schoolName);
            insertSchoolStmt.execute();

            // Commit insert
            conn.commit();
            Log.i(TAG, "Successfully registered school");
            // Rollback if there was a problem inserting
            conn.rollback();
        } else {
            do {
                remoteId = rs.getInt(1);
            } while (rs.next());
        }

        cleanup(resultSets, statements, conn);


        return remoteId;
    }

    private int registerUser(User user, int remoteSchoolId) {
        String email = user.getEmailAddress();
        int remoteId = 0;
        List<Statement> statements = new ArrayList<>();
        List<ResultSet> resultSets = new ArrayList<>();

        PreparedStatement selectUserStmt;
        Statement getNextUserIdStmt;
        PreparedStatement insertUserStmt;

        ResultSet rs;
        ResultSet nextUserIdRs;

        try {
            conn = DriverManager.getConnection(URL, APP_ID, PASS);
            conn.setAutoCommit(false);
            Log.i(TAG, "Successfully connected to database");

            String selectUserSql = SELECT_USER_ID_FROM_USER_SQL;
            selectUserStmt = conn.prepareStatement(selectUserSql);
            statements.add(selectUserStmt);
            selectUserStmt.setString(1, email);

            rs = selectUserStmt.executeQuery();
            resultSets.add(rs);

            if (!rs.next() || rs.getInt(1) == 0) {
                getNextUserIdStmt = conn.createStatement();
                statements.add(getNextUserIdStmt);
                nextUserIdRs = getNextUserIdStmt.executeQuery(SELECT_MAX_USER_ID_SQL);
                resultSets.add(nextUserIdRs);

                if (!nextUserIdRs.next()) {
                    remoteId = 1;
                } else {
                    do {
                        remoteId = nextUserIdRs.getInt(1) + 1;
                    } while (nextUserIdRs.next());
                }

                Log.d(TAG, "Inserting user with values [" + remoteId + ", " + user.getUserName() +
                        ", " + email + ", " + user.getRole() + ", " + remoteSchoolId + "]");
                String insertUserSql = INSERT_USER_SQL;
                insertUserStmt = conn.prepareStatement(insertUserSql);
                statements.add(insertUserStmt);
                insertUserStmt.setInt(1, remoteId);
                insertUserStmt.setInt(2, remoteSchoolId);
                insertUserStmt.setString(3, user.getUserName());
                insertUserStmt.setString(4, email);
                insertUserStmt.setString(5, user.getRole());
                insertUserStmt.execute();

                // Commit insert
                conn.commit();
                Log.i(TAG, "Successfully registered user");
                // Rollback if there was a problem inserting
                conn.rollback();
            } else {
                do {
                    remoteId = rs.getInt(1);
                } while (rs.next());
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Problem syncing user data: " + e.getMessage());
        } finally {
            cleanup(resultSets, statements, conn);
        }

        return remoteId;
    }

    private void getWalkthroughsAndResponses(int schoolId) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        List<Walkthrough> localWalkthroughs = walkthroughDao.getAll();
        Set<Walkthrough> remoteWalkthroughs = new HashSet<>(6);
        List<Response> remoteResponses = new ArrayList<>();

        conn = DriverManager.getConnection(URL, APP_ID, PASS);
        stmt = conn.prepareStatement(GET_WALKTHROUGHS_AND_RESPONSES_SQL);
        Log.d(TAG, "Looking up walkthroughs for schoolId: " + schoolId);
        stmt.setInt(1, schoolId);
        rs = stmt.executeQuery();

        while (rs.next()) {
            Walkthrough walkthrough = new Walkthrough(rs.getString("NAME"));
            walkthrough.setWalkthroughId(rs.getInt("WALKTHROUGH_ID"));

            Timestamp lastUpdatedTimestamp = rs.getTimestamp("LAST_UPDATED_DATE");
            Date lastUpdatedDate = new Date(lastUpdatedTimestamp.getTime());
            walkthrough.setLastUpdatedDate(lastUpdatedDate.toString());

            Timestamp createdTimestamp = rs.getTimestamp("CREATED_DATE");
            Date createdDate = new Date(createdTimestamp.getTime());
            walkthrough.setCreatedDate(createdDate.toString());

            walkthrough.setPercentComplete(rs.getFloat("PERCENT_COMPLETE"));

            remoteWalkthroughs.add(walkthrough);

            Response response = new Response();
            response.setResponseId(rs.getInt("RESPONSE_ID"));
            response.setWalkthroughId(rs.getInt("WALKTHROUGH_ID"));
            response.setUserId(1);
            response.setLocationId(rs.getInt("LOCATION_ID"));
            response.setQuestionId(rs.getInt("QUESTION_ID"));
            response.setActionPlan(rs.getString("ACTION_PLAN"));
            response.setPriority(rs.getInt("PRIORITY"));
            response.setRating(rs.getInt("RATING"));

            Timestamp remoteResponseTimestamp = rs.getTimestamp("TIMESTAMP");
            Date responseTimestamp = new Date(remoteResponseTimestamp.getTime());

            response.setTimeStamp(responseTimestamp.toString());
            response.setIsActionItem(rs.getInt("IS_ACTION_ITEM"));
            response.setImagePath(rs.getString("IMAGE_PATH"));

            remoteResponses.add(response);

        }

        for (Walkthrough w : remoteWalkthroughs) {
            Log.d(TAG, w.toString());
        }

        int maxLocalResponseId = prefManager.getLastResponseUniqueId();
        int maxRemoteResponseId = getMaxResponseId(remoteResponses);

        if (maxRemoteResponseId > maxLocalResponseId) {
            prefManager.setLastResponseUniqueId(maxRemoteResponseId);
        }

        // If no remote walkthroughs for the school, there's nothing to download
        for (Walkthrough localWalkthrough : localWalkthroughs) {
            Iterator<Walkthrough> iter = remoteWalkthroughs.iterator();

            while (iter.hasNext()) {
                Walkthrough remoteWalkthrough = iter.next();
                if (remoteWalkthrough.equals(localWalkthrough)) {
                    Log.d(TAG, "Walkthroughs are equal: remote=[" + remoteWalkthrough.getName() + "], local=[" + localWalkthrough.getName() + "]");
                    java.util.Date remoteLastUpdate = Utils.convertStringToDate(remoteWalkthrough.getLastUpdatedDate());
                    java.util.Date localLastUpdate = Utils.convertStringToDate(localWalkthrough.getLastUpdatedDate());

                    if (remoteLastUpdate.before(localLastUpdate)) {
                        Log.d(TAG, "Removing remote walkthrough from set");
                        iter.remove();
                    }
                }
            }

            Log.d(TAG, "remoteWalkthoughs.size(): " + remoteWalkthroughs.size());
        }

        if (remoteWalkthroughs.size() > 0) {
            Walkthrough[] walkthroughsArr = remoteWalkthroughs.toArray(new Walkthrough[0]);
            walkthroughDao.insertAll(walkthroughsArr);

            for (Walkthrough remoteWalkthrough : remoteWalkthroughs) {
                List<Response> responses = new ArrayList<>();
                for (Response remoteResponse : remoteResponses) {
                    if (remoteResponse.getWalkthroughId() == remoteWalkthrough.getWalkthroughId()) {
                        responses.add(remoteResponse);
                    }
                }

                responseDao.insertAll(responses);
            }
        }

        cleanupSimple(rs, stmt, conn);
    }

    private int getMaxResponseId(List<Response> remoteResponses) {
        int max = 0;

        for (Response r : remoteResponses) {
            Log.d(TAG, r.toString());
            if (r.getResponseId() > max) { max = r.getResponseId(); }
        }

        return max;
    }

    private void cleanupSimple(ResultSet rs, Statement stmt, Connection conn) {
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

    private void cleanup(List<ResultSet> resultSets, List<Statement> statements, Connection conn) {
        try {
            if (resultSets != null) {
                for (ResultSet rs : resultSets) {
                    if (rs != null) {
                        rs.close();
                    }
                }
            }

            if (statements != null) {
                for (Statement statement : statements) {
                    if (statement != null) {
                        statement.close();
                    }
                }
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            Log.e(TAG, sqle.getMessage());
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
