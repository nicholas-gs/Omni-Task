package com.example.ntu_timetable_calendar.Helper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A helper class for convert a boolean[] from the AddNewTaskFragment into a List<Long> that represents the
 * alarm/reminder timings
 */
public class AlarmParser {

    private boolean[] selectedAlarms;
    private Calendar deadlineCalendar;
    private List<Long> finalList = new ArrayList<>();

    public AlarmParser(@NonNull boolean[] selectedAlarms, @NonNull Calendar deadlineCalendar) {
        this.selectedAlarms = selectedAlarms;
        this.deadlineCalendar = deadlineCalendar;
    }

    public List<Long> parse() {

        ArrayList<Integer> pos = getPos();

        if (pos.size() != 0) {
            for (int i : pos) {
                Calendar tempCalendar = (Calendar) this.deadlineCalendar.clone();
                finalList.add(converter(i, tempCalendar).getTimeInMillis());
            }
        }
        return this.finalList;
    }

    /**
     * @return Return a list of positions of true boolean values in selectedAlarms
     */
    private ArrayList<Integer> getPos() {
        ArrayList<Integer> pos = new ArrayList<>();

        int i = 0;
        for (boolean b : selectedAlarms) {
            if (b) {
                pos.add(i);
            }
            i += 1;
        }
        return pos;
    }

    /**
     * @param i        Positions in the original boolean[] with boolean value true
     * @param calendar The original deadline calendar
     * @return Return a new Calendar object that represents the alarm timing
     */
    private Calendar converter(int i, Calendar calendar) {
        switch (i) {
            case 0:
                calendar.add(Calendar.MINUTE, -30);
                break;
            case 1:
                calendar.add(Calendar.HOUR_OF_DAY, -2);
                break;
            case 2:
                calendar.add(Calendar.HOUR_OF_DAY, -6);
                break;
            case 3:
                calendar.add(Calendar.HOUR_OF_DAY, -12);
                break;
            case 4:
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                break;
        }
        return calendar;
    }
}