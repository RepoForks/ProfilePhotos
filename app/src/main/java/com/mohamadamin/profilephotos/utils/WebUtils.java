package com.mohamadamin.profilephotos.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.mohamadamin.profilephotos.app.AppController;
import com.mohamadamin.profilephotos.modules.CustomApplication;

public class WebUtils {

    final static String URL_APPS = "http://mohamadamin.ir.tn/api/apps/apps.php";
    static int counter;

    public static void checkWebAppOffers(final Context context) {

        if (!PreferenceUtils.canShowAppOffer(context)) return;

        JsonArrayRequest request = new JsonArrayRequest(URL_APPS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                JSONObject jsonObject;
                CustomApplication application;

                try {
                    List<CustomApplication> applications = new ArrayList<>();
                    for (int i=0; i<jsonArray.length(); i++) {
                        jsonObject = (JSONObject) jsonArray.get(i);
                        application = new CustomApplication(
                                jsonObject.getString("titleText"),
                                jsonObject.getString("descriptionText"),
                                jsonObject.getString("packageName"),
                                jsonObject.getString("imageLink")
                        );
                        applications.add(application);
                    }
                    counter = 0;
                    offerApplicationIfNeeded(context, applications);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, null);

        AppController.getInstance().addToRequestQueue(request);

    }

    public static void offerApplicationIfNeeded(final Context context, final List<CustomApplication> list) {
        if (SdkUtils.isPackageInstalled(context, list.get(counter).packageName)) {
            counter++;
            if (counter < list.size()) offerApplicationIfNeeded(context, list);
        } else {
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageLoader.get(list.get(counter).imageLink, new ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    counter++;
                    if (counter < list.size()) offerApplicationIfNeeded(context, list);
                }
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        list.get(counter).iconBitmap = response.getBitmap();
                        NotificationUtils.showAppOfferNotification(context, list.get(counter));
                        PreferenceUtils.setLastAppOfferTime(context, System.currentTimeMillis());
                    } else {
                        counter++;
                        if (counter < list.size()) offerApplicationIfNeeded(context, list);
                    }
                }
            });
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        else return false;
    }

}
