package com.example.ntu_timetable_calendar.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ntu_timetable_calendar.MainActivity;
import com.example.ntu_timetable_calendar.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;
import java.util.Objects;

/**
 * Utility methods for AddNewTaskFragment and TaskDetailFragment
 */
public class TaskFragmentUtil extends TaskEntityConstants{

    private Context context;
    private FragmentActivity fragmentActivity;


    public TaskFragmentUtil(@NonNull Context context, @NonNull final FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Internal validation check to see if the Task's title and description that the user is trying to save is valid
     *
     * @param titleInputLayout       TextInputLayout for the title
     * @param descriptionInputLayout TextInputLayout for the description
     * @param mTitle                 Title Sstring the user is trying to save
     * @param mDescription           Description string the user is trying to save
     * @return boolean value true if valid title AND description
     */
    public boolean validationCheck(@NonNull TextInputLayout titleInputLayout, @NonNull TextInputLayout descriptionInputLayout,
                                   @NonNull String mTitle, @NonNull String mDescription) {

        titleInputLayout.setErrorEnabled(false);
        descriptionInputLayout.setErrorEnabled(false);

        boolean validTitle = true;
        boolean validDescription = true;

        if (mTitle.length() == 0) {
            titleInputLayout.setError(context.getString(R.string.title_is_empty));
            validTitle = false;
        }

        if (mDescription.length() == 0) {
            descriptionInputLayout.setError(context.getString(R.string.description_is_empty));
            validDescription = false;
        }

        return (validTitle && validDescription);
    }

    /**
     * Format the alarm text view's title based on the number of alarms chosen by user
     */
    public String formatAddAlarmTitle(boolean[] alarmTimingChosen) {

        int count = 0;

        for (boolean b : alarmTimingChosen) {
            if (b) {
                count++;
            }
        }

        if (count == 0) {
            return context.getString(R.string.add_alarm);
        } else if (count == 1) {
            return String.format(Locale.ENGLISH, "%d alarm", count);
        } else {
            return String.format(Locale.ENGLISH, "%d alarms", count);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    public AlertDialog initCloseFragmentDialog(final boolean launchedFromNotification) {
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
                initMainActivity(launchedFromNotification);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public AlertDialog initNoMainTimetableDialog(){
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void initMainActivity(boolean launchedFromNotification) {
        if (launchedFromNotification) {
            Intent intent = new Intent(fragmentActivity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            fragmentActivity.startActivity(intent);
        }
    }

}