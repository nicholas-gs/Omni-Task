package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.R;

public class NoMainTimetableAlertDialog {

    private Context context;
    private DialogInterface.OnClickListener positiveButtonClicked;

    public NoMainTimetableAlertDialog(@NonNull Context context, @NonNull DialogInterface.OnClickListener positiveButtonClicked) {
        this.context = context;
        this.positiveButtonClicked = positiveButtonClicked;
    }

    public AlertDialog build() {
        return new MyAlertDialogBuilder.Builder(this.context).setMessage(context.getString(R.string.no_main_timetable_dialog_message))
                .setPositiveButtonListener(this.positiveButtonClicked, context.getString(R.string.okay))
                .build();
    }

}