package com.example.ntu_timetable_calendar.utils.basefragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ntu_timetable_calendar.interfaces.BaseTaskFragmentInterface;

/**
 * Base Fragment for AddNewTaskFragment & TaskDetailFragment
 */

public abstract class BaseTaskFragment extends Fragment implements BaseTaskFragmentInterface, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void updateChooseClassTextView() {

    }

    @Override
    public void updateCurrentTimeTextViews() {

    }

    @Override
    public void updateAlarmTextView() {

    }

    @Override
    public void updatePriorityTextView() {

    }

    @Override
    public void initSaveDialog() {

    }

    @Override
    public void initCloseDialog() {

    }

    @Override
    public void initNoMainTimetableDialog() {

    }

    @Override
    public void initAlarmDialog() {

    }

    @Override
    public void initPriorityDialog() {

    }

    @Override
    public void saveTask() {

    }

     void test(){

    }
}
