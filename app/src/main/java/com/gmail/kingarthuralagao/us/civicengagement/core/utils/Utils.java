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

        return monthNameToNumber(month) + "/" + day + "/" + year ;
    }

    private static String monthNameToNumber(String month) {
        switch (month) {
            case "January" :
                return "1";
            case "February" :
                return "2";
            case "March" :
                return "3";
            case "April" :
                return "4";
            case "May" :
                return "5";
            case "June" :
                return "6";
            case "July" :
                return "7";
            case "August" :
                return "8";
            case "September" :
                return "9";
            case "October" :
                return "10";
            case "November" :
                return "11";
            default:
                return "12";
        }
    }


    public static int convertToStandardTime(int hour) {
        switch (hour) {
            case 13:
                return 1;
            case 14:
                return 2;
            case 15:
                return 3;
            case 16:
                return 4;
            case 17:
                return 5;
            case 18:
                return 6;
            case 19:
                return 7;
            case 20:
                return 8;
            case 21:
                return 9;
            case 22:
                return 10;
            case 23:
                return 11;
            default:
                return 12;
        }
    }

    public static String buildTimeString(int hour, int minute) {
        String amOrPm = "AM";
        String formattedMinute = String.valueOf(minute);

        if (hour >= 12) {
            amOrPm = "PM";
            hour = Utils.convertToStandardTime(hour); // Converts 24-hour based clock to 12-hour based
        }

        if (hour == 0) {
            hour = 12;
        }

        if (minute < 10) {
            formattedMinute = "0" + formattedMinute;
        }
        return hour + ":" + formattedMinute + " " + amOrPm;
    }

}
