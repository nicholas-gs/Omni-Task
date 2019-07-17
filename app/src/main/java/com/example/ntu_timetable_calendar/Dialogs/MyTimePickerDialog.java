package com.example.ntu_timetable_calendar.Dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyTimePickerDialog extends DialogFragment {

    // Pass in a calendar variable to set the current/chosen time
    private Calendar calendar;

    public MyTimePickerDialog(Calendar calendar){
        this.calendar = calendar;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new TimePickerDialog(requireContext(), (TimePickerDialog.OnTimeSetListener) getParentFragment(),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(requireContext()));
    }


}
