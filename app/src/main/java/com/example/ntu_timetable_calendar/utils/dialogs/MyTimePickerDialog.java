package com.example.ntu_timetable_calendar.utils.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ntu_timetable_calendar.R;

import java.util.Calendar;

public class MyTimePickerDialog extends DialogFragment {

    // Pass in a calendar variable to set the current/chosen time
    private Calendar calendar;

    public MyTimePickerDialog(Calendar calendar) {
        this.calendar = calendar;
    }

    public MyTimePickerDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return new TimePickerDialog(requireContext(), (TimePickerDialog.OnTimeSetListener) getParentFragment(),
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(requireContext()));
        } else {
            long l = savedInstanceState.getLong(getString(R.string.time_picker_dialog_saved_calendar_key));
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(l);
            this.calendar = c;
            return new TimePickerDialog(requireContext(), (TimePickerDialog.OnTimeSetListener) getParentFragment(),
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(requireContext()));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(getString(R.string.time_picker_dialog_saved_calendar_key), this.calendar.getTimeInMillis());
        super.onSaveInstanceState(outState);
    }
}