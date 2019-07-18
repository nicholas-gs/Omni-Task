package com.example.ntu_timetable_calendar.Helper;

/**
 * Enum that returns the Calendar.DAY_OF_WEEK for a given day string
 */

public enum DayOfWeek {

    SUN(1), MON(2), TUE(3), WED(4), THU(5), FRI(6), SAT(7);

    private final int value;

    DayOfWeek(int value) {

        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {

        return value + "";
    }

}
