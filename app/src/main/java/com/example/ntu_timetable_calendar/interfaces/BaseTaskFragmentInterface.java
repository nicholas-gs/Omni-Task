package com.example.ntu_timetable_calendar.interfaces;

import android.view.View;

public interface BaseTaskFragmentInterface {

    void initViews(View view);

    void updateChooseClassTextView();

    void updateCurrentTimeTextViews();

    void updateAlarmTextView();

    void updatePriorityTextView();


    // Dialogs

    void initSaveDialog();

    void initCloseDialog();

    void initNoMainTimetableDialog();

    void initAlarmDialog();

    void initPriorityDialog();

    ////////////////////////////////////////////////////////////////////////////////////////////////

    void saveTask();


}
