package com.plusmobileapps.safetyapp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionMappingDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.dao.SchoolDao;
import com.plusmobileapps.safetyapp.data.dao.UserDao;
import com.plusmobileapps.safetyapp.data.dao.WalkthroughDao;
import com.plusmobileapps.safetyapp.data.entity.Location;
import com.plusmobileapps.safetyapp.data.entity.QuestionMapping;
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
import java.util.List;

/**
 * Created by Robert Beerman on 2/25/2018.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    // Global variables
    private ContentResolver mContentResolver;
    private Connection conn;
    private PrefManager prefManager;
    private AppDatabase db;
    private SchoolDao schoolDao;
    private UserDao userDao;
    private WalkthroughDao walkthroughDao;
    private ResponseDao responseDao;
    private LocationDao locationDao;
    private QuestionMappingDao questionMappingDao;

    // Connection properties
    private static final String URL = "jdbc:mysql://10.0.2.2:3306/safetywalkthrough?useSSL=false";
    //private static final String URL = "jdbc:mysql://safetymysqlinstance.cbcumohyescr.us-west-2.rds.amazonaws.com:3306/safetywalkthrough?useSSL=false";
    //private static final String DB_NAME = "safetywalkthrough";
    private static final String APP_ID = "safety_app";
    private static final String PASS = "J7jd!ETRysdxrTGh";

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        prefManager = new PrefManager(context);
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        prefManager = new PrefManager(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        if (!prefManager.isUserSignedUp()) {
            Log.d(TAG, "PrefManager says user isn't signed up, so not running sync");
            return;
        }
        Log.d(TAG, "Performing sync");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        School school = null;
        User user = null;
        int remoteSchoolId = 0;
        int remoteUserId = 0;

        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        schoolDao = db.schoolDao();
        userDao = db.userDao();
        walkthroughDao = db.walkthroughDao();
        responseDao = db.responseDao();
        locationDao = db.locationDao();
        //questionDao = db.questionDao();
        questionMappingDao = db.questionMappingDao();

        school = schoolDao.get();
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
        } else {
            remoteUserId = user.getRemoteId();
            Log.d(TAG, "User " + user.getUserName() + " is already registered");
        }

        syncLocationsAndQuestions(remoteSchoolId);
        deleteRemoteWalkthroughs(remoteSchoolId, remoteUserId);
        // TODO Sync walkthroughs & responses
        syncWalkthroughsAndResponses(remoteSchoolId, remoteUserId);
    }

    private void deleteRemoteWalkthroughs(int remoteSchoolId, int remoteUserId) {
        List<Statement> statements = new ArrayList<>();
        List<Walkthrough> deletedWalkthroughs = walkthroughDao.getAllDeleted();
        PreparedStatement deleteWalkthroughsStmt;

        String deleteWalkthroughsSql = "DELETE FROM walkthroughs " +
                "WHERE schoolId = ? AND userId = ? AND walkthroughId = ?";

        try {
            conn = DriverManager.getConnection(URL, APP_ID, PASS);
            conn.setAutoCommit(false);
            Log.i(TAG, "Successfully connected to database");

            deleteWalkthroughsStmt = conn.prepareStatement(deleteWalkthroughsSql);
            statements.add(deleteWalkthroughsStmt);

            for (Walkthrough deletedWalkthrough : deletedWalkthroughs) {
                deleteWalkthroughsStmt.setInt(1, remoteSchoolId);
                deleteWalkthroughsStmt.setInt(2, remoteUserId);
                deleteWalkthroughsStmt.setInt(3, deletedWalkthrough.getWalkthroughId());
                deleteWalkthroughsStmt.addBatch();
            }

            int[] deletedCount = deleteWalkthroughsStmt.executeBatch();
            Log.i(TAG, "Deleted " + deletedCount.length + " walkthroughs");

            conn.commit();

            conn.rollback();

            if (deletedCount.length == deletedWalkthroughs.size()) {
                for (Walkthrough walkthroughToDelete : deletedWalkthroughs) {
                    walkthroughDao.delete(walkthroughToDelete);
                }
            } else {
                Log.i(TAG, "Inconsistent walkthrough deletion counts; not deleting local walkthroughs");
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Problem deleting walkthroughs: " + e.getMessage());
        } finally {
            cleanup(null, statements, conn);
        }
    }

    private void syncWalkthroughsAndResponses(int remoteSchoolId, int remoteUserId) {
        List<Statement> statements = new ArrayList<>();
        List<ResultSet> resultSets = new ArrayList<>();
        List<Walkthrough> activeWalkthroughs = walkthroughDao.getAll();
        PreparedStatement getLastSyncDateTimeStmt;
        PreparedStatement updateWalkthroughStmt;
        PreparedStatement updateResponseStmt;

        String getLastSyncDateTimeSql = "SELECT MAX(lastUpdatedDate) " +
                "FROM walkthroughs WHERE schoolId = ? AND userId = ?";

        String updateWalkthroughSql = "INSERT INTO walkthroughs " +
                "(walkthroughId, schoolId, userId, name, lastUpdatedDate, createdDate, percentComplete) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "name = ?, lastUpdatedDate = ?, percentComplete = ?";

        String updateResponseSql = "INSERT INTO responses " +
                "(responseId, schoolId, userId, walkthroughId, locationId, questionId, actionPlan, " +
                "priority, rating, timestamp, isActionItem, image) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "actionPlan = ?, priority = ?, rating = ?, timestamp = ?, isActionItem = ?, image = ?";

        ResultSet lastSyncDateTimeRs;
        Timestamp lastSyncDateTime = null;

        try {
            conn = DriverManager.getConnection(URL, APP_ID, PASS);
            conn.setAutoCommit(false);
            Log.i(TAG, "Successfully connected to database");

            getLastSyncDateTimeStmt = conn.prepareStatement(getLastSyncDateTimeSql);
            statements.add(getLastSyncDateTimeStmt);
            getLastSyncDateTimeStmt.setInt(1, remoteSchoolId);
            getLastSyncDateTimeStmt.setInt(2, remoteUserId);

            lastSyncDateTimeRs = getLastSyncDateTimeStmt.executeQuery();
            resultSets.add(lastSyncDateTimeRs);

            while(lastSyncDateTimeRs.next()) {
                lastSyncDateTime = lastSyncDateTimeRs.getTimestamp(1);
            }

            if (lastSyncDateTime != null) {
                Log.d(TAG, "Max last update date: " + lastSyncDateTime.toString());
            } else {
                Log.i(TAG, "No walkthroughs for schoolId " + remoteSchoolId + ", userId " + remoteUserId + " in db");
            }

            updateWalkthroughStmt = conn.prepareStatement(updateWalkthroughSql);
            statements.add(updateWalkthroughStmt);
            updateResponseStmt = conn.prepareStatement(updateResponseSql);
            statements.add(updateResponseStmt);

            for (Walkthrough walkthrough : activeWalkthroughs) {
                int walkthroughId = walkthrough.getWalkthroughId();
                java.util.Date lastUpdatedDate = Utils.convertStringToDate(walkthrough.getLastUpdatedDate());
                Timestamp newTimeStamp = new Timestamp(lastUpdatedDate.getTime());
                java.util.Date createdDate = Utils.convertStringToDate(walkthrough.getCreatedDate());
                Timestamp createdTimestamp = new Timestamp(createdDate.getTime());

                if (lastSyncDateTime == null || lastSyncDateTime.before(lastUpdatedDate)) {
                    updateWalkthroughStmt.setInt(1, walkthroughId);
                    updateWalkthroughStmt.setInt(2, remoteSchoolId);
                    updateWalkthroughStmt.setInt(3, remoteUserId);
                    updateWalkthroughStmt.setString(4, walkthrough.getName());
                    updateWalkthroughStmt.setTimestamp(5, newTimeStamp);
                    updateWalkthroughStmt.setTimestamp(6, createdTimestamp);
                    updateWalkthroughStmt.setDouble(7, walkthrough.getPercentComplete());
                    updateWalkthroughStmt.setString(8, walkthrough.getName());
                    updateWalkthroughStmt.setTimestamp(9, newTimeStamp);
                    updateWalkthroughStmt.setDouble(10, walkthrough.getPercentComplete());
                    //Log.d(TAG, updateWalkthroughStmt.toString());
                    updateWalkthroughStmt.addBatch();

                    List<Response> responses = responseDao.getAllByWalkthroughId(walkthrough.getWalkthroughId());

                    for (Response response : responses) {
                        updateResponseStmt.setInt(1, response.getResponseId());
                        updateResponseStmt.setInt(2, remoteSchoolId);
                        updateResponseStmt.setInt(3, remoteUserId);
                        updateResponseStmt.setInt(4, walkthroughId);
                        updateResponseStmt.setInt(5, response.getLocationId());
                        updateResponseStmt.setInt(6, response.getQuestionId());
                        updateResponseStmt.setString(7, response.getActionPlan());
                        updateResponseStmt.setInt(8, response.getPriority());
                        updateResponseStmt.setInt(9, response.getRating());
                        updateResponseStmt.setString(10, response.getTimeStamp());
                        updateResponseStmt.setInt(11, response.getIsActionItem());
                        updateResponseStmt.setString(12, response.getImagePath());
                        updateResponseStmt.setString(13, response.getActionPlan());
                        updateResponseStmt.setInt(14, response.getPriority());
                        updateResponseStmt.setInt(15, response.getRating());
                        updateResponseStmt.setString(16, response.getTimeStamp());
                        updateResponseStmt.setInt(17, response.getIsActionItem());
                        updateResponseStmt.setString(18, response.getImagePath());
                        Log.d(TAG, updateResponseStmt.toString());
                        updateResponseStmt.addBatch();
                    }
                }
            }

            int[] walkthroughCount = updateWalkthroughStmt.executeBatch();
            Log.i(TAG, "Upddated " + walkthroughCount.length + " walkthroughs");

            int[] responseCount = updateResponseStmt.executeBatch();
            Log.i(TAG, "Updated " + responseCount.length + " responses");

            conn.commit();

            conn.rollback();

        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Problem syncing walkthrough or response data: " + e.getMessage());
        } finally {
            cleanup(resultSets, statements, conn);
        }
    }

    private void syncLocationsAndQuestions(int remoteSchoolId) {
        List<Location> locations = locationDao.getAllLocations();
        List<QuestionMapping> questionMappings = questionMappingDao.getAllQuestionMappings();

        List<Statement> statements = new ArrayList<>();
        PreparedStatement locationStmt;
        PreparedStatement questionMappingStmt;
        String locationSql = "REPLACE INTO location (locationId, schoolId, name, type, locationInstruction) " +
                "VALUES (?, ?, ?, ?, ?)";
        String questionMappingSql = "REPLACE INTO question_mapping (mappingId, schoolId, locationId, questionId) " +
                "VALUES (?, ?, ?, ?)";

        if (locations == null || locations.isEmpty()) {
            return;
        }

        try {
            conn = DriverManager.getConnection(URL, APP_ID, PASS);
            conn.setAutoCommit(false);
            locationStmt = conn.prepareStatement(locationSql);
            statements.add(locationStmt);
            questionMappingStmt = conn.prepareStatement(questionMappingSql);
            statements.add(questionMappingStmt);

            for (Location location : locations) {
                locationStmt.setInt(1, location.getLocationId());
                locationStmt.setInt(2, remoteSchoolId);
                locationStmt.setString(3, location.getName());
                locationStmt.setString(4, location.getType());
                locationStmt.setString(5, location.getLocationInstruction());
                locationStmt.addBatch();
            }

            int[] locationCount = locationStmt.executeBatch();
            Log.i(TAG, "Synced " + locationCount.length + " locations");

            for (QuestionMapping questionMapping : questionMappings) {
                questionMappingStmt.setInt(1, questionMapping.getMappingId());
                questionMappingStmt.setInt(2, remoteSchoolId);
                questionMappingStmt.setInt(3, questionMapping.getLocationId());
                questionMappingStmt.setInt(4, questionMapping.getQuestionId());
                questionMappingStmt.addBatch();
            }

            int[] questionMappingCount = questionMappingStmt.executeBatch();
            Log.i(TAG, "Synced " + questionMappingCount.length + " question mappings");

            conn.commit();

            conn.rollback();
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Problem syncing location or question data: " + e.getMessage());
        } finally {
            cleanup(null, statements, conn);
        }
    }

    private int registerSchool(School school) {
        String schoolName = school.getSchoolName();
        int remoteId = 0;
        List<Statement> statements = new ArrayList<>();
        List<ResultSet> resultSets = new ArrayList<>();

        PreparedStatement selectSchoolStmt;
        Statement getNextSchoolIdStmt;
        PreparedStatement insertSchoolStmt;

        ResultSet rs;
        ResultSet nextSchoolIdRs;

        try {
            conn = DriverManager.getConnection(URL, APP_ID, PASS);
            conn.setAutoCommit(false);
            Log.i(TAG, "Successfully connected to database");

            String selectSchoolSql = "SELECT schoolId FROM schools WHERE schoolName = ?";
            selectSchoolStmt = conn.prepareStatement(selectSchoolSql);
            statements.add(selectSchoolStmt);
            selectSchoolStmt.setString(1, schoolName);

            rs = selectSchoolStmt.executeQuery();
            resultSets.add(rs);

            if (!rs.next() || rs.getInt(1) == 0) {
                getNextSchoolIdStmt = conn.createStatement();
                statements.add(getNextSchoolIdStmt);
                nextSchoolIdRs = getNextSchoolIdStmt.executeQuery("SELECT MAX(schoolId) " +
                        "FROM schools");
                resultSets.add(nextSchoolIdRs);

                if (!nextSchoolIdRs.next()) {
                    remoteId = 1;
                } else {
                    do {
                        remoteId = nextSchoolIdRs.getInt(1) + 1;
                    } while (nextSchoolIdRs.next());
                }

                Log.d(TAG, "Inserting school with values [" + remoteId + ", " + schoolName +"]");
                String insertSchoolSql = "INSERT INTO schools VALUES (?, ?)";
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
        } catch(Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Problem syncing school data: " + e.getMessage());
        } finally {
            cleanup(resultSets, statements, conn);
        }

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

            String selectUserSql = "SELECT userId FROM user WHERE emailAddress = ?";
            selectUserStmt = conn.prepareStatement(selectUserSql);
            statements.add(selectUserStmt);
            selectUserStmt.setString(1, email);

            rs = selectUserStmt.executeQuery();
            resultSets.add(rs);

            if (!rs.next() || rs.getInt(1) == 0) {
                getNextUserIdStmt = conn.createStatement();
                statements.add(getNextUserIdStmt);
                nextUserIdRs = getNextUserIdStmt.executeQuery("SELECT MAX(userId) " +
                        "FROM user");
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
                String insertUserSql = "INSERT INTO user (userId, schoolId, userName, emailAddress, role) " +
                        "VALUES (?, ?, ?, ?, ?)";
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
}