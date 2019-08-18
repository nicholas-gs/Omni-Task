package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.R;

/**
 * Display a AlertDialog prompting user to save the task
 */
public class SaveTaskAlertDialog {

    private Context context;
    private DialogInterface.OnClickListener positiveButtonClicked, negativeButtonClicked;

    public SaveTaskAlertDialog(@NonNull Context context, @NonNull DialogInterface.OnClickListener positiveButtonClicked,
                               @NonNull DialogInterface.OnClickListener negativeButtonClicked) {
        this.context = context;
        this.positiveButtonClicked = positiveButtonClicked;
        this.negativeButtonClicked = negativeButtonClicked;
    }

    public AlertDialog build() {
        String positiveButtonText = context.getString(R.string.save);
        String negativeButtonText = context.getString(R.string.keep_editing);
        return new MyAlertDialogBuilder.Builder(context).setMessage(context.getString(R.string.save_new_task_dialog_message))
                .setPositiveButtonListener(this.positiveButtonClicked, positiveButtonText)
                .setNegativeButtonListener(this.negativeButtonClicked, negativeButtonText).build();
    }
}