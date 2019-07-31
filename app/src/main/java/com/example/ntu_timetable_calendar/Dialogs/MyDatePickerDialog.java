package com.example.ntu_timetable_calendar.Dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ntu_timetable_calendar.R;

import java.util.Calendar;

public class MyDatePickerDialog extends DialogFragment {

    private Calendar calendar;

    public MyDatePickerDialog(Calendar calendar) {
        this.calendar = calendar;
    }

    public MyDatePickerDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return new android.app.DatePickerDialog(requireContext(), (android.app.DatePickerDialog.OnDateSetListener) getParentFragment()
                    , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            long l = savedInstanceState.getLong(getString(R.string.date_picker_dialog_saved_calendar_key));
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(l);
            this.calendar = c;
            return new android.app.DatePickerDialog(requireContext(), (android.app.DatePickerDialog.OnDateSetListener) getParentFragment()
                    , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(getString(R.string.date_picker_dialog_saved_calendar_key), this.calendar.getTimeInMillis());
        super.onSaveInstanceState(outState);
    }
}