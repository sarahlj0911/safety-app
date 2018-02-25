package com.plusmobileapps.safetyapp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Robert Beerman on 2/25/2018.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    Connection conn;

    private static final String url = "jdbc:mysql://10.0.2.2:3306/safetywalkthrough";
    private static final String user = "safety_app";
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

        testDB();
    }

    public void testDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
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
                conn.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                Log.d(TAG, sqle.getMessage());
            }
        }
    }
}
