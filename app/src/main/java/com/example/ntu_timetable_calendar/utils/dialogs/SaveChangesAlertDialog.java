package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.R;

public class SaveChangesAlertDialog {

    private Context context;
    private DialogInterface.OnClickListener positiveButtonClicked, negativeButtonClicked;

    public SaveChangesAlertDialog(@NonNull Context context, @NonNull DialogInterface.OnClickListener positiveButtonClicked,
                                  @NonNull DialogInterface.OnClickListener negativeButtonClicked) {
        this.context = context;
        this.positiveButtonClicked = positiveButtonClicked;
        this.negativeButtonClicked = negativeButtonClicked;
    }

    public AlertDialog build() {
        return new MyAlertDialogBuilder.Builder(this.context).setMessage(context.getString(R.string.save_changes_dialog_message))
                .setPositiveButtonListener(this.positiveButtonClicked, context.getString(R.string.save))
                .setNegativeButtonListener(this.negativeButtonClicked, context.getString(R.string.cancel)).build();
    }
}
