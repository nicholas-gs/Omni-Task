package com.example.ntu_timetable_calendar.utils.constants;

/**
 * Constants for the TaskEntity
 */
public abstract class TaskEntityConstants {

    @SuppressWarnings("FieldCanBeLocal")
    private static int PRIORITY_1 = 1;
    @SuppressWarnings("FieldCanBeLocal")
    private static int PRIORITY_2 = 2;
    @SuppressWarnings("FieldCanBeLocal")
    private static int PRIORITY_3 = 3;
    @SuppressWarnings("FieldCanBeLocal")
    private static int PRIORITY_4 = 4;
    @SuppressWarnings("FieldCanBeLocal")
    private static int NO_PRIORITY = 5;

    public static int getPriority1() {
        return PRIORITY_1;
    }

    public static int getPriority2() {
        return PRIORITY_2;
    }

    public static int getPriority3() {
        return PRIORITY_3;
    }

    public static int getPriority4() {
        return PRIORITY_4;
    }

    public static int getNoPriority() {
        return NO_PRIORITY;
    }
}
