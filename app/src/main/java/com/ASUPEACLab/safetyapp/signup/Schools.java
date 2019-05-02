package com.ASUPEACLab.safetyapp.signup;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class  Schools {
    static String json = null;
    public static List<String> schoolNames = new LinkedList<>();
    public static JSONObject obj;

    static void load(Context context) {

        try {
            File file = new File("/data/data/com.plusmobileapps.safetyapp/databases/schools.json");
            FileInputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        try{
            obj = (JSONObject) new JSONTokener(json).nextValue();
            JSONArray keys = obj.names();
            for(int i = 0; i < keys.length(); i++) {
                Log.d("json", keys.getString(i));
                schoolNames.add(keys.getString(i));
            }

            Log.d("json", schoolNames.get(1));
        }catch(JSONException ex){
            ex.printStackTrace();
        }

    }
}
