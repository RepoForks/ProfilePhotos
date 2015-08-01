package com.mohamadamin.profilephotos.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class SdkUtils {

    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException exception) {
            return false;
        }
    }

    public static boolean isJellyBeanMR1OrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

}
