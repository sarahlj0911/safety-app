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
import java.util.List;

/**
 * Created by Robert Beerman on 2/25/2018.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Connection conn;
    private AppDatabase db;
    private SchoolDao schoolDao;
    private UserDao userDao;

    // Connection properties
    private static final String url = "jdbc:mysql://10.0.2.2:3306/safetywalkthrough";
    //private static final String url = "jdbc:mysql://safetymysqlinstance.cbcumohyescr.us-west-2.rds.amazonaws.com:3306/safetywalkthrough";
    private static final String dbName = "safetywalkthrough";
    private static final String appId = "safety_app";
    private static final String pass = "J7jd!ETRysdxrTGh";

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
    }

    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        schoolDao = db.schoolDao();
        userDao = db.userDao();

        if (testConnection()) {
            syncSchoolAndUser();
        }
    }

    public boolean testConnection() {
        try {
            conn = DriverManager.getConnection(url, appId, pass);
            Log.d(TAG, "Successfully connected to database on host [" + url + "]");
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            Log.d(TAG, sqle.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                Log.d(TAG, sqle.getMessage());
            }
        }

        return true;
    }

    public void testReadFromDB() {
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, appId, pass);
            Log.d(TAG, "Database connection success!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from schools");
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                Log.d(TAG, rsmd.getColumnName(1) + ": " + rs.getInt(1));
                Log.d(TAG, rsmd.getColumnName(2) + ": " + rs.getString(2));
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                Log.d(TAG, sqle.getMessage());
            }
        }
    }

    public void testWriteToDB() {
        try {
            conn = DriverManager.getConnection(url, appId, pass);
            conn.setAutoCommit(true);
            Log.d(TAG, "Connected to database on " + url);

            List<School> schoolList = schoolDao.getAll();
            //int schoolId = 0;
            String schoolName = "";

            if (schoolList != null && !schoolList.isEmpty()) {
                School school = schoolList.get(0);
                //schoolId = school.getSchoolId();
                schoolName = school.getSchoolName();
            }

            PreparedStatement insertSchool = null;
            String insertSchoolString = "REPLACE INTO " + dbName + ".schools VALUES (?)";
            insertSchool = conn.prepareStatement(insertSchoolString);
            //insertSchool.setInt(1, schoolId);
            insertSchool.setString(1, schoolName);

            if (insertSchool.execute()) {
                Log.d(TAG, "Insert successful!");
            }

        } catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                Log.d(TAG, sqle.getMessage());
            }
        }
    }

    public boolean syncSchoolAndUser() {
        boolean schoolSynced = false;
        boolean userSynced = false;
        School school = null;
        User user = null;
        String schoolName = "";
        int remoteSchoolId = 0;
        String userEmail = "";
        int remoteUserId = 0;

        List<School> schoolList = schoolDao.getAll();

        if (schoolList != null && !schoolList.isEmpty()) {
            school = schoolList.get(0);

            if (school.getRemoteId() > 0) {
                schoolSynced = true;
            }

            schoolName = school.getSchoolName();
        }

        user = userDao.getUser();

        if (user.getRemoteId() > 0) {
            userSynced = true;
        }

        userEmail = user.getEmailAddress();

        try {
            conn = DriverManager.getConnection(url, appId, pass);
            Log.d(TAG, "Successfully connected to database on host [" + url + "]");

            PreparedStatement selectSchoolIdStmt = null;
            String selectSchool = "select schoolId from schools where schoolName = ?";
            selectSchoolIdStmt = conn.prepareStatement(selectSchool);
            selectSchoolIdStmt.setString(1, schoolName);

            ResultSet rs = selectSchoolIdStmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                remoteSchoolId = rs.getInt(1);
                school.setRemoteId(remoteSchoolId);
                schoolDao.insert(school);
            }

            if (!rs.next()) {
                Statement maxSchoolIdStmt = conn.createStatement();
                ResultSet maxSchoolIdRs = maxSchoolIdStmt.executeQuery("select max(schoolId) from schools");

                while (maxSchoolIdRs.next()) {
                    remoteSchoolId = maxSchoolIdRs.getInt(1);
                }

            }


        } catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Problem syncing school data: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                Log.d(TAG, sqle.getMessage());
            }
        }

        return true;
    }

    public boolean syncUser() {

        return true;
    }


}
