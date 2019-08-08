package com.example.ntu_timetable_calendar.Helper;

public abstract class BooleanArrayHelper {

    /**
     * @param array boolean array to check
     * @return Returns true if all boolean values in the array are false
     */
    public static boolean AreAllFalse(boolean[] array) {
        for (boolean b : array) if (b) return false;
        return true;
    }
}
