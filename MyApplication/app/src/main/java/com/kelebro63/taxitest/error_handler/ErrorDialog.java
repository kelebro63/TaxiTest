package com.kelebro63.taxitest.error_handler;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.kelebro63.taxitest.R;
import com.kelebro63.taxitest.base.BaseActivity;


public class ErrorDialog {
    public static void showRetryDialog(@NonNull Context context, @NonNull String error, @NonNull DialogInterface.OnClickListener positive) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.dialogs_error)
                .setMessage(error)
                .setCancelable(false)
                .setNegativeButton(R.string.dialogs_cancel, (dialog, which) -> {
                    dialog.dismiss();
                    ((BaseActivity) context).onBackPressed();
                });
                //.setPositiveButton(R.string.dialogs_repeat, positive).show();
    }

    public static void showRetryDialog(@NonNull Context context, @StringRes int errorResId, @NonNull DialogInterface.OnClickListener positive) {
        showRetryDialog(context, context.getString(errorResId), positive);
    }

    public static void show(Context context, @StringRes int errorDescriptionResId) {
        show(context, context.getString(errorDescriptionResId));
    }

    public static void show(Context context, String errorDescription) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.dialogs_error)
                .setMessage(errorDescription)
                .setPositiveButton(R.string.dialogs_ok, (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }
}
