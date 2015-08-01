package com.mohamadamin.profilephotos.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import com.mohamadamin.profilephotos.R;

import com.mohamadamin.profilephotos.activities.MainActivity;
import com.mohamadamin.profilephotos.modules.CustomApplication;

public class NotificationUtils {

    public static void showAppUpdateNotification(Context context) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("bazaar://details?id=com.mohamadamin.profilephotos"));
        intent.setPackage("com.farsitel.bazaar");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                .setContentTitle(context.getResources().getString(R.string.new_version))
                .setContentText(context.getResources().getString(R.string.new_version_details))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(context.getResources().getString(R.string.new_version_details))
                .setBigContentTitle(context.getResources().getString(R.string.new_version))
                .setSummaryText(context.getResources().getString(R.string.new_version_details));
        notificationBuilder.setStyle(bigTextStyle);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(-42, notificationBuilder.build());

    }

    public static void showAppOfferNotification(Context context, CustomApplication application) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("bazaar://details?id=" + application.packageName));
        intent.setPackage("com.farsitel.bazaar");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_icon)
                .setLargeIcon((application.iconBitmap == null) ?
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.icon) : application.iconBitmap)
                .setContentTitle(application.titleText)
                .setContentText(application.descriptionText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(application.descriptionText)
                .setBigContentTitle(application.titleText)
                .setSummaryText(application.descriptionText);
        notificationBuilder.setStyle(bigTextStyle);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(-43, notificationBuilder.build());

    }

}
