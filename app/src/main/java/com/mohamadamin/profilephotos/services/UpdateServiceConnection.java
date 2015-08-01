package com.mohamadamin.profilephotos.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.dynamixsoftware.ErrorAgent;
import com.farsitel.bazaar.IUpdateCheckService;

import com.mohamadamin.profilephotos.utils.NotificationUtils;


public class UpdateServiceConnection implements ServiceConnection {

    IUpdateCheckService updateCheckService;
    Context context;

    public UpdateServiceConnection(Context context) {
        this.context = context;
    }

    public void onServiceConnected(ComponentName name, IBinder boundService) {

        updateCheckService = IUpdateCheckService.Stub.asInterface(boundService);

        try {
            long versionCode = updateCheckService.getVersionCode("com.mohamadamin.profilephotos");
            if (versionCode != -1) NotificationUtils.showAppUpdateNotification(context);
        } catch (Exception e) {
            ErrorAgent.reportError(e, "UpdateServiceConnection.class");
        }

    }

    public void onServiceDisconnected(ComponentName name) {
        updateCheckService = null;
    }

}
