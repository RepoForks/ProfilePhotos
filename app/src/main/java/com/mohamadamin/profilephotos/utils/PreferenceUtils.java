package com.mohamadamin.profilephotos.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtils {
	
	public static String SHOULD_SHOW_RATING="shouldShowRating",
			LAST_APP_OFFER_TIME = "lastAppOfferTime",
			IS_PREMIUM="isPremium",
			SHOULD_SHOW_ADS = "shouldShowAds";

	public static boolean shouldShowRating(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(SHOULD_SHOW_RATING, true);
	}

	public static boolean canShowAppOffer(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		long lastTime = sharedPreferences.getLong(LAST_APP_OFFER_TIME, -1L);
		return System.currentTimeMillis() > (lastTime + (3 * (24 * 60 * 60 * 1000)));
	}

	public static boolean isPremium(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(IS_PREMIUM, false);
	}

	public static boolean shouldShowAds(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(SHOULD_SHOW_ADS, true);
	}

	public static void setShouldShowRating(Context context, boolean shouldShowRating) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(SHOULD_SHOW_RATING, shouldShowRating);
		editor.apply();
	}

	public static void setLastAppOfferTime(Context context, long timeInMillis) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putLong(LAST_APP_OFFER_TIME, timeInMillis);
		editor.apply();
	}

	public static void setIsPremium(Context context, boolean isPremium) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(IS_PREMIUM, isPremium);
		editor.apply();
	}

	public static void setShouldShowAds(Context context, boolean shouldShowAds) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(SHOULD_SHOW_ADS, shouldShowAds);
		editor.apply();
	}

}
