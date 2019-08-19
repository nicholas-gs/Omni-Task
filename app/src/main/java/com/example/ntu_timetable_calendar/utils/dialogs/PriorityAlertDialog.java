package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.R;

public class PriorityAlertDialog {

    private Context context;
    private DialogInterface.OnClickListener positiveButtonClicked, singleItemClickedListener;
    private int checkedItem;

    public PriorityAlertDialog(@NonNull Context context, @NonNull DialogInterface.OnClickListener positiveButtonClicked,
                               @NonNull DialogInterface.OnClickListener itemClickedListener, int checkedItem) {
        this.context = context;
        this.positiveButtonClicked = positiveButtonClicked;
        this.singleItemClickedListener = itemClickedListener;
        this.checkedItem = checkedItem;
    }

    public AlertDialog build() {
        String[] listItems = context.getResources().getStringArray(R.array.priority_dialog_selections);
        String message = context.getString(R.string.choose_priority);

        return new MyAlertDialogBuilder.Builder(this.context).setPositiveButtonListener(this.positiveButtonClicked, context.getString(R.string.confirm))
                .setTitle(message).setSingleChoiceItems(listItems, this.checkedItem, this.singleItemClickedListener).build();
    }
}
