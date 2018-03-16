package com.plusmobileapps.safetyapp.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rbeerma on 3/8/2018.
 */

public class Utils {
    private static final String TAG = "Utils";

    public static Date convertStringToDate(String dateStr) {
        // Thu Mar 08 07:45:05 MST 2018
        Date parsedDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);

        try {
            parsedDate = formatter.parse(dateStr);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing string to date: " + e.getMessage());
            e.printStackTrace();
        }

        return parsedDate;
    }

}
