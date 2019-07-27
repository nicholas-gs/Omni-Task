package com.example.ntu_timetable_calendar.Helper;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.Entity.DetailEntity;

import java.util.Calendar;

/**
 * Helper class to convert the String which defines the time (e.g "1130" or "1430") into a Calendar object.
 * Currently used by CourseEventToCourseEventEntityConverter.java and CourseEntityToEventConverter.java classes
 */
public abstract class CalendarParser {

    public static final int START_TIME = 0;
    public static final int END_TIME = 1;

    public static Calendar parseTime(int type, @NonNull Calendar firstDay, @NonNull DetailEntity detailEntity) {

        if (type == START_TIME) {
            return getStartCalendar(firstDay, detailEntity);
        } else if (type == END_TIME) {
            return getEndCalendar(firstDay, detailEntity);
        } else {
            throw new IllegalArgumentException("Not valid output type defined");
        }
    }

    /**
     * @param firstDay     Calendar represents the first day of the school term
     * @param detailEntity DetailEntity to convert
     * @return Calendar object representing the start of the DetailEntity after conversion
     */
    private static Calendar getStartCalendar(@NonNull Calendar firstDay, @NonNull DetailEntity detailEntity) {

        Calendar calendar = (Calendar) firstDay.clone();

        String startTimeStrHour = detailEntity.getTime().getStart().substring(0, 2);
        String startTimeStrMin = detailEntity.getTime().getStart().substring(2, 4);

        // Set time
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeStrHour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(startTimeStrMin));
        calendar.set(Calendar.SECOND, 0);

        // Set day
        calendar.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(detailEntity.getDay()).getValue());

        // For whatever reason, we have to call .get() in order for the calendar object to internalize the changes made to it!
        calendar.get(Calendar.DAY_OF_MONTH);

        return calendar;
    }


    /**
     * @param firstDay     Calendar represents the first day of the school term
     * @param detailEntity DetailEntity to convert
     * @return Calendar object representing the end of the DetailEntity after conversion
     */
    private static Calendar getEndCalendar(@NonNull Calendar firstDay, @NonNull DetailEntity detailEntity) {

        Calendar calendar = (Calendar) firstDay.clone();
        String endTimeStrHour = detailEntity.getTime().getEnd().substring(0, 2);
        String endTimeStrMin = detailEntity.getTime().getEnd().substring(2, 4);

        // Set time
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeStrHour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(endTimeStrMin));
        calendar.set(Calendar.SECOND, 0);

        // Set day
        calendar.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(detailEntity.getDay()).getValue());

        // For whatever reason, we have to call .get() in order for the calendar object to internalize the changes made to it!
        calendar.get(Calendar.DAY_OF_MONTH);

        return calendar;
    }

}
