package com.cs360.gunnardullecs360project;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

//Class for popup alert on request of SMS notifications
public class AlertSMSNotification {

    public static AlertDialog doubleButton(final ListActivity context) {
        //Builder class for dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.alert_sms_title)
                .setCancelable(false)
                .setMessage(R.string.alert_sms_message)
                .setPositiveButton(R.string.alert_sms_enable_button, (dialog, arg1) -> {
                    Toast.makeText(context, "SMS alerts are enabled", Toast.LENGTH_LONG).show();
                    ListActivity.AllowSMS();
                    dialog.cancel();
                })
                .setNegativeButton(R.string.alert_sms_disable_button, (dialog, arg1) -> {
                    Toast.makeText(context, "SMS alerts are disabled", Toast.LENGTH_LONG).show();
                    ListActivity.DenySMS();
                    dialog.cancel();
                });
        //AlertDialog object return
        return builder.create();
    }
}
