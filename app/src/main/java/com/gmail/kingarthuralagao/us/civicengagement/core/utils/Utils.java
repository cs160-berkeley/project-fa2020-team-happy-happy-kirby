package com.gmail.kingarthuralagao.us.civicengagement.core.utils;

import android.content.res.Resources;
import android.text.TextUtils;

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
}
