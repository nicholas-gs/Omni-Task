package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ntu_timetable_calendar.R;
import com.example.ntu_timetable_calendar.utils.InitialiseBackStack;

import java.util.Objects;

public class CloseFragmentDialog {

    private Context context;
    private FragmentActivity fragmentActivity;
    private boolean launchedFromNotification;

    public CloseFragmentDialog(Context context, FragmentActivity fragmentActivity, boolean launchedFromNotification) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.launchedFromNotification = launchedFromNotification;
    }

    public AlertDialog initCloseFragmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.task_detail_fragment_close_dialog_message));
        builder.setNegativeButton(context.getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(context.getString(R.string.discard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InitialiseBackStack.initMainActivity(fragmentActivity, launchedFromNotification);
                Objects.requireNonNull(fragmentActivity).finish();
            }
        });

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
