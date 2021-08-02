package com.project.nikhil.secfamfinal1.utils;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalculateTimeAgo extends Application {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long timestamp){
        if (timestamp < 1000000000000L){
            //if timestamp given in seconds, convert to millis
            timestamp *= 1000;
        }

        long currentTime = System.currentTimeMillis();
        if (timestamp > currentTime || timestamp <= 0){
            return "";
        }

        final long differance = currentTime - timestamp;
        if (differance < MINUTE_MILLIS){
            return "just now";
        } else if (differance < 2 * MINUTE_MILLIS){
            return "a minute ago";
        } else if (differance < 50 * MINUTE_MILLIS){
            return differance / MINUTE_MILLIS + " minute ago";
        } else if (differance < 90 * MINUTE_MILLIS){
            return "an hour ago";
        } else if (differance < 24 * HOUR_MILLIS){
            return differance / HOUR_MILLIS + " hours ago";
        } else if (differance < 48 * HOUR_MILLIS){
            return "yesterday";
        } else {
            return differance / DAY_MILLIS + " days ago";
        }
    }


    //get chat Date
    public static String getChatDate(long timestamp) {
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


        Date timestampDate = new Date();
        timestampDate.setTime(timestamp);
        long now = System.currentTimeMillis();

        //the last message was sent today return today
        if (isSameDay(now, timestamp)) {
            return "TODAY";
            //the last message was sent yesterday return yesterday
        } else if (isYesterday(now, timestamp)) {
            return "YESTERDAY";
        } else {
            //otherwise show the date of last message
            return fullDateFormat.format(timestampDate);
        }
    }
    //check if it's same day for the header date
    // if it's same day we will not show a new header
    public static boolean isSameDay(long timestamp1, long timestamp2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(timestamp1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(timestamp2);
        boolean sameYear = isSameYear(calendar1, calendar2);
        boolean sameMonth = isSameMonth(calendar1, calendar2);
        boolean sameDay = isSameDay(calendar1, calendar2);
        return (sameDay && sameMonth && sameYear);
    }
    /*
       NOTE:timestamp1 should be greater that timestamp2 in order to give a correct result
        */
    private static boolean isYesterday(long timestamp1, long timestamp2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(timestamp1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(timestamp2);
        boolean isYesterday = calendar1.get(Calendar.DAY_OF_MONTH) - 1 == calendar2.get(Calendar.DAY_OF_MONTH);


        return isSameYear(calendar1, calendar2) && isSameMonth(calendar1, calendar2) && isYesterday;
    }

    private static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    private static boolean isSameMonth(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
    }

    private static boolean isSameYear(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }
}
