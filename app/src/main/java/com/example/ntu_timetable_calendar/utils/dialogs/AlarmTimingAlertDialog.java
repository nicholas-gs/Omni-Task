package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.R;

public class AlarmTimingAlertDialog {

    private Context context;
    private DialogInterface.OnClickListener positiveButtonClicked, negativeButtonClicked, neutralButtonClicked;
    private DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener;
    private boolean[] checkedItems;

    public AlarmTimingAlertDialog(@NonNull Context context, @NonNull DialogInterface.OnClickListener positiveButtonClicked, @NonNull DialogInterface.OnClickListener negativeButtonClicked, @NonNull DialogInterface.OnClickListener neutralButtonClicked,
                                  @NonNull DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener, @NonNull boolean[] checkedItems) {
        this.context = context;
        this.positiveButtonClicked = positiveButtonClicked;
        this.negativeButtonClicked = negativeButtonClicked;
        this.neutralButtonClicked = neutralButtonClicked;
        this.multiChoiceClickListener = multiChoiceClickListener;
        this.checkedItems = checkedItems;
    }

    public AlertDialog build() {
        return new MyAlertDialogBuilder.Builder(this.context).setTitle(context.getString(R.string.alarm_timing))
                .setMultiChoiceItems(context.getResources().getStringArray(R.array.alarm_timing_dialog_selections), this.checkedItems, this.multiChoiceClickListener)
                .setPositiveButtonListener(this.positiveButtonClicked, context.getString(R.string.confirm)).
                        setNegativeButtonListener(this.negativeButtonClicked, context.getString(R.string.clear))
                .setNeutralButtonListener(this.neutralButtonClicked, context.getString(R.string.custom)).build();
    }
}
