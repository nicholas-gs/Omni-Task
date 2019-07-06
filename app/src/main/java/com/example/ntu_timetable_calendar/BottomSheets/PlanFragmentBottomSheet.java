package com.example.ntu_timetable_calendar.BottomSheets;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ntu_timetable_calendar.Helper.BottomNavColor;
import com.example.ntu_timetable_calendar.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PlanFragmentBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_fragment_bottomsheet_layout, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Set Bottom Nav Color to white instead of faded out
        Activity activity = getActivity();
        if (activity != null) {
            BottomNavColor.setWhiteNavigationBar(dialog, getActivity());
        }

        return dialog;
    }
}
