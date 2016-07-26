package com.kelebro63.taxitest.error_handler;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.kelebro63.taxitest.R;

public class GoogleApiException extends Exception {
    public GoogleApiException() {
    }

    public GoogleApiException(String detailMessage) {
        super(detailMessage);
    }

    public GoogleApiException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public GoogleApiException(Throwable throwable) {
        super(throwable);
    }

    public static void displayErrorDialog(FragmentActivity activity) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle(R.string.dialog_error)
                .setMessage(R.string.dialogs_location_cannot_be_determined)
                .setNegativeButton(R.string.dialogs_exit, (dialog, which) -> {
                    activity.finish();
                })
                .setPositiveButton(R.string.dialogs_ok, (dialog, which) -> {
                    String packageName = "com.google.android.gms";
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    } catch (ActivityNotFoundException exception) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }
                }).show();
    }
}
