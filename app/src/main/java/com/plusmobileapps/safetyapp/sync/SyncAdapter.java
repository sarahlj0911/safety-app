package com.plusmobileapps.safetyapp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.plusmobileapps.safetyapp.BuildConfig;
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
import com.plusmobileapps.safetyapp.util.DateTimeUtil;
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
 * Created by Robert Beerman on 2/25/2018.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    public static final String COM_MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

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
    private static final String URL = BuildConfig.DB_URL;
    private static final String APP_ID = BuildConfig.APP_ID;
    private static final String PASS = BuildConfig.SYNC_PASS;

    // SQL statement constants
    public static final String SELECT_MAX_SCHOOL_ID_SQL = "SELECT MAX(schoolId) FROM schools";
    public static final String INSERT_SCHOOL_SQL = "INSERT INTO schools VALUES (?, ?)";
    public static final String SELECT_USER_ID_FROM_USER_SQL = "SELECT userId FROM user WHERE emailAddress = ?";
    public static final String SELECT_MAX_USER_ID_SQL = "SELECT MAX(userId) FROM user";
    public static final String INSERT_USER_SQL = "INSERT INTO user (userId, schoolId, userName, " +
            "emailAddress, role) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_WALKTHROUGH_SQL = "DELETE FROM walkthroughs " +
            "WHERE schoolId = ? AND userId = ? AND walkthroughId = ?";
    private static final String GET_LAST_SYNC_DATETIME_SQL = "SELECT MAX(lastUpdatedDate) " +
            "FROM walkthroughs WHERE schoolId = ?";

    private static final String UPDATE_WALKTHROUGH_SQL = "INSERT INTO walkthroughs " +
            "(walkthroughId, schoolId, userId, name, lastUpdatedDate, createdDate, percentComplete) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "name = ?, lastUpdatedDate = ?, percentComplete = ?, userId = ?";
    private static final String UPDATE_RESPONSE_SQL = "INSERT INTO responses " +
            "(responseId, schoolId, userId, walkthroughId, locationId, questionId, actionPlan, " +
            "priority, rating, timestamp, isActionItem, image) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "actionPlan = ?, priority = ?, rating = ?, timestamp = ?, isActionItem = ?, image = ?";
    private static final String LOCATION_SQL = "REPLACE INTO location (locationId, schoolId, name, " +
            "type, locationInstruction) VALUES (?, ?, ?, ?, ?)";
    private static final String QUESTION_MAPPING_SQL = "REPLACE INTO question_mapping " +
            "(mappingId, schoolId, locationId, questionId) VALUES (?, ?, ?, ?)";
    private static final String SELECT_SCHOOL_SQL = "SELECT schoolId FROM schools WHERE schoolName = ?";

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
        Log.i(TAG, "Performing sync");

        try {
            Class.forName(COM_MYSQL_JDBC_DRIVER);
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

        try {
            downloadWalkthroughsAndResponses(remoteSchoolId);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            Log.e(TAG, "Problem downloading waltkhrough data: " + sqle.getMessage());
        }
        //deleteRemoteWalkthroughs(remoteSchoolId, remoteUserId);
        uploadWalkthroughsAndResponses(remoteSchoolId, remoteUserId);
    }

    private void deleteRemoteWalkthroughs(int remoteSchoolId, int remoteUserId) {
        List<Statement> statements = new ArrayList<>();
        List<Walkthrough> deletedWalkthroughs = walkthroughDao.getAllDeleted();
        PreparedStatement deleteWalkthroughsStmt;

        try {
            conn = DriverManager.getConnection(URL, APP_ID, PASS);
            conn.setAutoCommit(false);
            Log.i(TAG, "Successfully connected to database");

            deleteWalkthroughsStmt = conn.prepareStatement(DELETE_WALKTHROUGH_SQL);
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

    private void uploadWalkthroughsAndResponses(int remoteSchoolId, int remoteUserId) {
        List<Statement> statements = new ArrayList<>();
        List<ResultSet> resultSets = new ArrayList<>();
        List<Walkthrough> activeWalkthroughs = walkthroughDao.getAll();
        PreparedStatement getLastSyncDateTimeStmt;
        PreparedStatement updateWalkthroughStmt;
        PreparedStatement updateResponseStmt;
        ResultSet lastSyncDateTimeRs;
        Timestamp lastSyncDateTime = null;

        try {
            conn = DriverManager.getConnection(URL, APP_ID, PASS);
            conn.setAutoCommit(false);
            Log.i(TAG, "Successfully connected to database");

            getLastSyncDateTimeStmt = conn.prepareStatement(GET_LAST_SYNC_DATETIME_SQL);
            statements.add(getLastSyncDateTimeStmt);
            getLastSyncDateTimeStmt.setInt(1, remoteSchoolId);

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

            updateWalkthroughStmt = conn.prepareStatement(UPDATE_WALKTHROUGH_SQL);
            statements.add(updateWalkthroughStmt);
            updateResponseStmt = conn.prepareStatement(UPDATE_RESPONSE_SQL);
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
                    updateWalkthroughStmt.setInt(11, remoteUserId);
                    updateWalkthroughStmt.addBatch();

                    List<Response> responses = responseDao.getAllByWalkthroughId(walkthrough.getWalkthroughId());

                    for (Response response : responses) {
                        java.util.Date responseTimestamp = Utils.convertStringToDate(response.getTimeStamp());
                        Timestamp newResponseTimeStamp = new Timestamp(responseTimestamp.getTime());
                        updateResponseStmt.setInt(1, response.getResponseId());
                        updateResponseStmt.setInt(2, remoteSchoolId);
                        updateResponseStmt.setInt(3, remoteUserId);
                        updateResponseStmt.setInt(4, walkthroughId);
                        updateResponseStmt.setInt(5, response.getLocationId());
                        updateResponseStmt.setInt(6, response.getQuestionId());
                        updateResponseStmt.setString(7, response.getActionPlan());
                        updateResponseStmt.setInt(8, response.getPriority());
                        updateResponseStmt.setInt(9, response.getRating());
                        //updateResponseStmt.setString(10, response.getTimeStamp());
                        updateResponseStmt.setTimestamp(10, newResponseTimeStamp);
                        updateResponseStmt.setInt(11, response.getIsActionItem());
                        updateResponseStmt.setString(12, response.getImagePath());
                        updateResponseStmt.setString(13, response.getActionPlan());
                        updateResponseStmt.setInt(14, response.getPriority());
                        updateResponseStmt.setInt(15, response.getRating());
                        //updateResponseStmt.setString(16, response.getTimeStamp());
                        updateResponseStmt.setTimestamp(16, newResponseTimeStamp);
                        updateResponseStmt.setInt(17, response.getIsActionItem());
                        updateResponseStmt.setString(18, response.getImagePath());
                        //Log.d(TAG, updateResponseStmt.toString());
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

    private void downloadWalkthroughsAndResponses(int schoolId) throws SQLException {
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
            walkthrough.setLastUpdatedDate(DateTimeUtil.getDateTimeString(lastUpdatedTimestamp.getTime()));

            Timestamp createdTimestamp = rs.getTimestamp("CREATED_DATE");
            walkthrough.setCreatedDate(DateTimeUtil.getDateTimeString(createdTimestamp.getTime()));

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
            //response.setTimeStamp(rs.getString("TIMESTAMP"));
            Timestamp remoteResponseTimestamp = rs.getTimestamp("TIMESTAMP");


            response.setTimeStamp(DateTimeUtil.getDateTimeString(remoteResponseTimestamp.getTime()));
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

    private void syncLocationsAndQuestions(int remoteSchoolId) {
        List<Location> locations = locationDao.getAllLocations();
        List<QuestionMapping> questionMappings = questionMappingDao.getAllQuestionMappings();
        List<Statement> statements = new ArrayList<>();
        PreparedStatement locationStmt;
        PreparedStatement questionMappingStmt;

        if (locations == null || locations.isEmpty()) {
            return;
        }

        try {
            conn = DriverManager.getConnection(URL, APP_ID, PASS);
            conn.setAutoCommit(false);
            locationStmt = conn.prepareStatement(LOCATION_SQL);
            statements.add(locationStmt);
            questionMappingStmt = conn.prepareStatement(QUESTION_MAPPING_SQL);
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
}