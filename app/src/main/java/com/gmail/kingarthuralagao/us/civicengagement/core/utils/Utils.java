package com.gmail.kingarthuralagao.us.civicengagement.core.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class Utils {

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static Long getTimeStampFromDate(String date) {
        long epoch = 0L;
        try {
            epoch = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(date).getTime() / 1000;

            Log.i("EventsViewFragment", "Timestamp: " + epoch);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return epoch;
    }

    public static String compareTwoTimeStamps(Long currentTime, Long timeOfPost)
    {
        long diff = currentTime - timeOfPost;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String ago;
        if(diffSeconds < 60)
            ago = diffSeconds + " seconds ago";
        else if(diffMinutes < 60)
            ago = diffMinutes + " minutes ago";
        else if(diffHours < 24)
            ago = diffHours+ " hours ago";
        else
            ago = diffDays + " days ago";

        return ago;
    }

    public static String getDateFromTimeStamp(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp * 1000);
        // cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        //String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (timeStamp*1000));

        int year = cal.get(Calendar.YEAR);
        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);  // 15
        int minute = cal.get(Calendar.MINUTE);  // 16
        int second = cal.get(Calendar.SECOND);  // 17
        return month + " " + day + " " + year + " at " + (hour%12) + " " + minute + " " + (hour > 12 && hour < 24 ? "PM" : "AM");
    }
}
