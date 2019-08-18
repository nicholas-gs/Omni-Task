package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.ntu_timetable_calendar.R;

public class SaveTaskDialog {

    private Context context;
    private DialogInterface.OnClickListener positiveButtonClicked, negativeButtonClicked;

    private SaveTaskDialog(Builder builder) {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Builder {

        private Context context;
        private DialogInterface.OnClickListener positiveButtonClicked, negativeButtonClicked;

        private Builder() {

        }

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder SetPositiveButtonClick(@NonNull DialogInterface.OnClickListener onClickListener) {
            this.positiveButtonClicked = onClickListener;
            return this;
        }

        public Builder SetNegativeButtonClick(@NonNull DialogInterface.OnClickListener onClickListener) {
            this.negativeButtonClicked = onClickListener;
            return this;
        }


        public AlertDialog build() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.save_new_task_dialog_message));
            builder.setPositiveButton(context.getString(R.string.save), positiveButtonClicked);
            builder.setNegativeButton(context.getString(R.string.keep_editing), negativeButtonClicked);

            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(context,
                            android.R.color.background_light));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(context,
                            android.R.color.background_light));
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,
                            R.color.colorPrimaryDark));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context,
                            R.color.colorPrimaryDark));
                }
            });

            return alertDialog;
        }
    }

}
