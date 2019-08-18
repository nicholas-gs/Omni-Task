package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ntu_timetable_calendar.R;

public class NoMainTimetableDialog {

    private Context context;
    private FragmentActivity fragmentActivity;

    public NoMainTimetableDialog(@NonNull Context context, @NonNull FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    public AlertDialog initNoMainTimetableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
        builder.setMessage(context.getString(R.string.no_main_timetable_dialog_message));
        builder.setPositiveButton(context.getString(R.string.okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(fragmentActivity,
                        android.R.color.background_light));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(fragmentActivity,
                        R.color.colorPrimaryDark));

            }
        });

        return alertDialog;
    }

}