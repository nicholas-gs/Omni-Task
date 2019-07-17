package com.example.ntu_timetable_calendar.Dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyDatePickerDialog extends DialogFragment {

    private Calendar calendar;

    public MyDatePickerDialog(Calendar calendar) {
        this.calendar = calendar;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new android.app.DatePickerDialog(requireContext(), (android.app.DatePickerDialog.OnDateSetListener) getParentFragment()
                , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }
}
