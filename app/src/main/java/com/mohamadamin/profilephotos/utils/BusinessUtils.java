package com.mohamadamin.profilephotos.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.dynamixsoftware.ErrorAgent;
import com.mohamadamin.profilephotos.R;
import com.mohamadamin.profilephotos.services.UpdateServiceConnection;

public class BusinessUtils {

    static UpdateServiceConnection updateServiceConnection;

    public static void launchOtherApps(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("bazaar://collection?slug=by_author&aid=mohamad_amin"));
            intent.setPackage("com.farsitel.bazaar");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.install_cafebazaar), Toast.LENGTH_LONG).show();
        }
    }

    public static void launchRatingWindow(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setData(Uri.parse("bazaar://details?id=" + context.getPackageName()));
            intent.setPackage("com.farsitel.bazaar");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.install_cafebazaar), Toast.LENGTH_LONG).show();
        }
    }

    public static void launchInstagramPage(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse("http://instagram.com/_u/mohamad__amin"));
            intent.setPackage("com.instagram.android");
            context.startActivity(intent);
        } catch (Exception ignored) {
            intent.setData(Uri.parse("http://instagram.com/mohamad__amin"));
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.instagram_page)));
        }
    }

    public static void showAutoRatingWindow(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.rating))
                .setMessage(context.getResources().getString(R.string.rating_description))
                .setPositiveButton(context.getResources().getString(R.string.rating_i_rate), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        launchRatingWindow(context);
                        PreferenceUtils.setShouldShowRating(context, false);
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.rating_later), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).finish();
                    }
                });
        builder.create().show();
    }

    public static void intitUpdateService(Context context) {
        updateServiceConnection = new UpdateServiceConnection(context);
        Intent intent = new Intent("com.farsitel.bazaar.service.UpdateCheckService.BIND");
        intent.setPackage("com.farsitel.bazaar");
        context.bindService(intent, updateServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public static void releaseUpdateService(Context context) {
        try {
            if (updateServiceConnection != null) {
                context.unbindService(updateServiceConnection);
                updateServiceConnection = null;
            }
        } catch (Exception exception) {
            ErrorAgent.reportError(exception, "BusinessUtils.releaseUpdateService()");
        }
    }

    public static void sendEmailToDeveloper(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"torpedo.mohammadi@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "[Profile Photos]");
        try {
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.communication_send_email)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, context.getResources().getString(R.string.communication_send_mail_no_application), Toast.LENGTH_SHORT).show();
        }
    }

    public static void showUpgradeDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.upgrade_to_premium))
                .setMessage(activity.getResources().getString(R.string.upgrade_to_premium_description))
                .setPositiveButton(R.string.upgrade, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IABUtils.buyPremiumVersion(activity);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
        builder.create().show();
    }

}
