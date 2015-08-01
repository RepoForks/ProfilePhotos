package com.mohamadamin.profilephotos.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageUtils {

    public static String getPersianNumbers(String string) {
        string = string.replace("0", "۰");
        string = string.replace("1", "١");
        string = string.replace("2", "۲");
        string = string.replace("3", "۳");
        string = string.replace("4", "۴");
        string = string.replace("5", "۵");
        string = string.replace("6", "۶");
        string = string.replace("7", "۷");
        string = string.replace("8", "۸");
        string = string.replace("9", "۹");
        return string;
    }

    public static Locale setPersianLocale(Context context) {
        Locale defaultLocale = Locale.getDefault();
        Locale locale = new Locale("fa");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        return defaultLocale;
    }

    public static void setDefaultLocale(Context context, Locale defaultLocale) {
        Locale.setDefault(defaultLocale);
        Configuration configuration = new Configuration();
        configuration.locale = defaultLocale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

}
