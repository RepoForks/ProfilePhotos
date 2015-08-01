package com.mohamadamin.profilephotos.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dynamixsoftware.ErrorAgent;
import com.mohamadamin.profilephotos.utils.cafebazaar.IabHelper;
import com.mohamadamin.profilephotos.utils.cafebazaar.IabResult;
import com.mohamadamin.profilephotos.utils.cafebazaar.Inventory;
import com.mohamadamin.profilephotos.utils.cafebazaar.Purchase;

public class IABUtils {

    static IabHelper iabHelper;

    final static int BUY_PREMIUM_REQUEST = 3441, REMOVE_ADS = 3442;
    final static String SKU_PREMIUM = "profilePhotosPremium",
                        SKU_NO_ADS = "profilePhotosNoAds";

    public static void initializeIabHelper(final Context context) {
        iabHelper = new IabHelper(context, "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDOMKv7jV0y7P4ciS5w6HiM/jb7zmzJo1T9VCji2/xVOo0rLcYSYH7+7T3EL4A+iJBZnwOgFv2rbgC93+3Sa7TLvy0lDNvBkU0pgv0/wEK6B3AZfCetzV8kZFnALntPJHOLOPQofFwcwoTZ9VT2wtr+WRqwkp9lVTy71nz0/LWtC13Nh0LYsX7XCXcnYe4rYXpfzYMJGt2Wfru9MV4ybZVfEu+PXOCh7x9BEjqKfzsCAwEAAQ==");
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) checkPurchases(context);
            }
        });
    }

    public static void destroyIabHelper() {
        if (iabHelper != null) iabHelper.dispose();
        iabHelper = null;
    }

    public static void buyPremiumVersion(final Activity activity) {
        try {
            if (iabHelper == null) initializeIabHelper(activity);
            iabHelper.launchPurchaseFlow(activity,
                    SKU_PREMIUM,
                    BUY_PREMIUM_REQUEST,
                    new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase info) {
                            if (result.isSuccess()) {
                                PreferenceUtils.setIsPremium(activity.getBaseContext(), true);
                                Toast.makeText(activity,
                                        "خرید شما موفقیت آمیز بود. برای مشاهده ی تغییرات برنامه را از اول اجرا کنید."
                                        , Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(activity,
                                        "خطا، خرید شما موفقیت آمیز نبود!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, "");
        } catch (Exception e) {
            ErrorAgent.reportError(e, "IABUtils.buyPremiumVersion()");
            Toast.makeText(activity, "مشکلی‌ پیش‌ آمده، لطفا مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
        }
    }

    public static void removeAds(final Activity activity) {
        try {
            if (iabHelper == null) initializeIabHelper(activity);
            iabHelper.launchPurchaseFlow(activity,
                    SKU_NO_ADS,
                    REMOVE_ADS,
                    new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase info) {
                            if (result.isSuccess()) {
                                PreferenceUtils.setShouldShowAds(activity, false);
                                Toast.makeText(activity,
                                        "خرید شما موفقیت آمیز بود. برای مشاهده ی تغییرات برنامه را از اول اجرا کنید."
                                        , Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(activity.getBaseContext(),
                                        "خطا، خرید شما موفقیت آمیز نبود!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, "");
        } catch (Exception e) {
            ErrorAgent.reportError(e, "IABUtils.removeAds()");
            Toast.makeText(activity, "مشکلی‌ پیش‌ آمده، لطفا مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
        }
    }

    public static void checkPurchases(final Context context) {
        try {
            if (iabHelper == null) return;
            iabHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                    if (result.isSuccess()) {
                        PreferenceUtils.setIsPremium(context, inventory.hasPurchase(SKU_PREMIUM));
                        PreferenceUtils.setShouldShowAds(context, inventory.hasPurchase(SKU_NO_ADS));
                    }
                }
            });
        } catch (Exception e) {
            ErrorAgent.reportError(e, "IABUtils.checkPurchases()");
        }
    }

    public static boolean handleResult(int requestCode, int resultCode, Intent data) {
        try {
            if (iabHelper == null) return false;
            return iabHelper.handleActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            ErrorAgent.reportError(e, "IABUtils.handleResult()");
            return false;
        }
    }

}
