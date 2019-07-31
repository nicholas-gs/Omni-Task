package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Calendar;
import java.util.List;

public class AddNewTaskFragmentViewModel extends AndroidViewModel {

    public AddNewTaskFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    private Calendar deadLineCalendar;
    private boolean[] alarmTimingChosen;
    private Integer priorityChosen;
    private List<Long> listOfAlarms;

    // Setters

    public void setDeadLineCalendar(Calendar deadLineCalendar) {
        this.deadLineCalendar = deadLineCalendar;
    }

    public void setAlarmTimingChosen(boolean[] alarmTimingChosen) {
        this.alarmTimingChosen = alarmTimingChosen;
    }

    public void setPriorityChosen(Integer priorityChosen) {
        this.priorityChosen = priorityChosen;
    }

    public void setListOfAlarms(List<Long> listOfAlarms) {
        this.listOfAlarms = listOfAlarms;
    }

    // Getters

    public Calendar getDeadLineCalendar() {
        return deadLineCalendar;
    }

    public boolean[] getAlarmTimingChosen() {
        return alarmTimingChosen;
    }

    public Integer getPriorityChosen() {
        return priorityChosen;
    }

    public List<Long> getListOfAlarms() {
        return listOfAlarms;
    }
}