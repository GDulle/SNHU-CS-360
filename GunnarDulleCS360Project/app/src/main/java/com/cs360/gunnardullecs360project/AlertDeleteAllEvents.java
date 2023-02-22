package com.cs360.gunnardullecs360project;

import androidx.appcompat.app.AlertDialog;

//Class for popup alert on delete all

public class AlertDeleteAllEvents {

    public static AlertDialog doubleButton(final ListActivity context) {

        //Builder class for dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.alert_delete_title)
                .setCancelable(false)
                .setMessage(R.string.alert_delete_message)
                .setPositiveButton(R.string.alert_delete_dialog_yes_button, (dialog, arg1) -> {
                    ListActivity.YesDeleteEvents();
                    dialog.cancel();
                })
                .setNegativeButton(R.string.alert_delete_dialog_no_button, (dialog, arg1) -> {
                    ListActivity.NoDeleteEvents();
                    dialog.cancel();
                });
        //AlertDialog object return
        return builder.create();
    }
}
