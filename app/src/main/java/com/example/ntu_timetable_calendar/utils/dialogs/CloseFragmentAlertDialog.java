package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.R;

/**
 * Display a AlertDialog confirming that user wants to close the fragment
 */
public class CloseFragmentAlertDialog {

    private Context context;
    private DialogInterface.OnClickListener positiveButtonClicked, negativeButtonClicked;

    public CloseFragmentAlertDialog(@NonNull Context context, @NonNull DialogInterface.OnClickListener positiveButtonClicked,
                                    @NonNull DialogInterface.OnClickListener negativeButtonClicked) {
        this.context = context;
        this.positiveButtonClicked = positiveButtonClicked;
        this.negativeButtonClicked = negativeButtonClicked;
    }

    public AlertDialog build() {
        return new MyAlertDialogBuilder.Builder(this.context).setMessage(context.getString(R.string.task_detail_fragment_close_dialog_message))
                .setPositiveButtonListener(this.positiveButtonClicked, context.getString(R.string.discard))
                .setNegativeButtonListener(this.negativeButtonClicked, context.getString(R.string.keep_editing))
                .build();
    }
}