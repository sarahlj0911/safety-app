package com.plusmobileapps.safetyapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

public class FileUtil {
    public static boolean deleteDb(Context context) {


        File file = new File("/data/data/com.plusmobileapps.safetyapp/databases/appDB.db");
        if(file.exists())
            file.delete();

        String s = "@@@" +  context.getDatabasePath("appDB");




        boolean deleted = file.exists();
        String b = "true";
        if(deleted == false) {
            b = "false";
        }
        Log.d(s, b);


        return true;
    }


}
