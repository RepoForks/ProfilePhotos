package com.mohamadamin.profilephotos.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mohamadamin.profilephotos.utils.WebUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WebUtils.isNetworkAvailable(context)) WebUtils.checkWebAppOffers(context);
    }

}
