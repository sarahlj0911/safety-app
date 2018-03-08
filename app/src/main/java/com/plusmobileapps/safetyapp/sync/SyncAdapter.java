package com.plusmobileapps.safetyapp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.PrefManager;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.SchoolDao;
import com.plusmobileapps.safetyapp.data.dao.UserDao;
import com.plusmobileapps.safetyapp.data.entity.School;
import com.plusmobileapps.safetyapp.data.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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

    // Connection properties
    private static final String url = "jdbc:mysql://10.0.2.2:3306/safetywalkthrough";
    //private static final String url = "jdbc:mysql://safetymysqlinstance.cbcumohyescr.us-west-2.rds.amazonaws.com:3306/safetywalkthrough?useSSL=false";
    private static final String dbName = "safetywalkthrough";
    private static final String appId = "safety_app";
    private static final String pass = "";

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
        boolean userRegistered = false;
        int remoteSchoolId = 0;
        String userEmail = "";
        int remoteUserId = 0;

        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        schoolDao = db.schoolDao();
        userDao = db.userDao();

        school = schoolDao.get();
        if (!school.isRegistered()) {
            Log.d(TAG, "School not registered");
            remoteSchoolId = registerSchool(school);
            school.setRemoteId(remoteSchoolId);
            schoolDao.insert(school);
            Log.d(TAG, "Registered school remote id = " + remoteSchoolId);
        } else {
            Log.d(TAG, "School " + school.getSchoolName() + " is already registered");
        }

        user = userDao.getUser();
        if (!user.isRegistered()) {
            Log.d(TAG, "User not registered");
            remoteUserId = registerUser(user, remoteSchoolId);
            user.setRemoteId(remoteUserId);
            userDao.insert(user);
            Log.d(TAG, "Registered user remote id = " + remoteUserId);
        } else {
            Log.d(TAG, "User " + user.getUserName() + " is already registered");
        }

    }

    private int registerSchool(School school) {
        String schoolName = school.getSchoolName();
        int remoteId = 0;
        List<Statement> statements = new ArrayList<>();
        List<ResultSet> resultSets = new ArrayList<>();

        PreparedStatement selectSchoolStmt = null;
        Statement getNextSchoolId = null;
        PreparedStatement insertSchoolStmt = null;

        ResultSet rs = null;
        ResultSet nextSchoolIdRs = null;

        try {
            conn = DriverManager.getConnection(url, appId, pass);
            conn.setAutoCommit(false);
            Log.d(TAG, "Successfully connected to database on host [" + url + "]");

            String selectSchool = "SELECT schoolId FROM schools WHERE schoolName = ?";
            selectSchoolStmt = conn.prepareStatement(selectSchool);
            statements.add(selectSchoolStmt);
            selectSchoolStmt.setString(1, schoolName);

            rs = selectSchoolStmt.executeQuery();
            resultSets.add(rs);

            if (!rs.next() || rs.getInt(1) == 0) {
                getNextSchoolId = conn.createStatement();
                statements.add(getNextSchoolId);
                nextSchoolIdRs = getNextSchoolId.executeQuery("SELECT MAX(schoolId) " +
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
                String insertSchool = "INSERT INTO schools VALUES (?, ?)";
                insertSchoolStmt = conn.prepareStatement(insertSchool);
                statements.add(insertSchoolStmt);
                insertSchoolStmt.setInt(1, remoteId);
                insertSchoolStmt.setString(2, schoolName);
                insertSchoolStmt.execute();

                // Commit insert
                conn.commit();
                Log.d(TAG, "Successfully registered school");
                // Rollback if there was a problem inserting
                conn.rollback();
            } else {
                do {
                    remoteId = rs.getInt(1);
                } while (rs.next());
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Problem syncing school data: " + e.getMessage());
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

        PreparedStatement selectUserStmt = null;
        Statement getNextUserId = null;
        PreparedStatement insertUserStmt = null;

        ResultSet rs = null;
        ResultSet nextUserIdRs = null;

        try {
            conn = DriverManager.getConnection(url, appId, pass);
            conn.setAutoCommit(false);
            Log.d(TAG, "Successfully connected to database on host [" + url + "]");

            String selectUser = "SELECT userId FROM user WHERE emailAddress = ?";
            selectUserStmt = conn.prepareStatement(selectUser);
            statements.add(selectUserStmt);
            selectUserStmt.setString(1, email);

            rs = selectUserStmt.executeQuery();
            resultSets.add(rs);

            if (!rs.next() || rs.getInt(1) == 0) {
                getNextUserId = conn.createStatement();
                statements.add(getNextUserId);
                nextUserIdRs = getNextUserId.executeQuery("SELECT MAX(userId) " +
                        "FROM user");
                resultSets.add(nextUserIdRs);

                if (!nextUserIdRs.next()) {
                    remoteId = 1;
                } else {
                    do {
                        remoteId = nextUserIdRs.getInt(1) + 1;
                    } while (nextUserIdRs.next());
                }

                Log.d(TAG, "Inserting user with values [" + remoteId + ", " + user.getUserName() + ", " + email + ", " + user.getRole() + ", " + remoteSchoolId + "]");
                String insertUser = "INSERT INTO user VALUES (?, ?, ?, ?, ?)";
                insertUserStmt = conn.prepareStatement(insertUser);
                statements.add(insertUserStmt);
                insertUserStmt.setInt(1, remoteId);
                insertUserStmt.setString(2, user.getUserName());
                insertUserStmt.setString(3, email);
                insertUserStmt.setString(4, user.getRole());
                insertUserStmt.setInt(5, remoteSchoolId);
                insertUserStmt.execute();

                // Commit insert
                conn.commit();
                Log.d(TAG, "Successfully registered user");
                // Rollback if there was a problem inserting
                conn.rollback();
            } else {
                do {
                    remoteId = rs.getInt(1);
                } while (rs.next());
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Problem syncing school data: " + e.getMessage());
        } finally {
            cleanup(resultSets, statements, conn);
        }

        return remoteId;
    }

    private void cleanup(List<ResultSet> resultSets, List<Statement> statements, Connection conn) {
        try {
            for (ResultSet rs : resultSets) {
                if (rs != null) {
                    rs.close();
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            Log.d(TAG, sqle.getMessage());
        }
    }

}
