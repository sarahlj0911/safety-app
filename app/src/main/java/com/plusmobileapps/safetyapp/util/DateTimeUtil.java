package com.plusmobileapps.safetyapp.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {

    private static final String DATE_TIME_FORMAT = "EEE MMM dd HH:mm:ss zzz";
    private static final String ARIZONA_TIME_ZONE = "US/Arizona";

    /**
     * format date time string to MST time zone
     *
     * @return
     */
    public static String getDateTimeString() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ARIZONA_TIME_ZONE));
        return dateFormat.format(new Date());
    }

    public static String getDateTimeString(long milliseconds) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ARIZONA_TIME_ZONE));
        return dateFormat.format(new Date(milliseconds));
    }

}
