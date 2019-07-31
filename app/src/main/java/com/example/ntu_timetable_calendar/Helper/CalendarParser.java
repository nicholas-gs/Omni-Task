package com.example.ntu_timetable_calendar.Helper;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.Entity.DetailEntity;
import com.example.ntu_timetable_calendar.Enum.DayOfWeek;
import com.example.ntu_timetable_calendar.Enum.MonthOfYear;
import com.example.ntu_timetable_calendar.JsonModels.Exam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Helper class to convert the String which defines the time (e.g "1130" or "1430") into a Calendar object.
 * 1) parseEventTime(...) used by CourseEventToCourseEventEntityConverter.java and CourseEntityToEventConverter.java classes for Event object conversion.
 * 2)
 */
public abstract class CalendarParser {

    public static final int START_TIME = 0;
    public static final int END_TIME = 1;

    public static Calendar parseEventTime(int type, @NonNull Calendar firstDay, @NonNull DetailEntity detailEntity) {

        if (type == START_TIME) {
            return getEventStartCalendar(firstDay, detailEntity);
        } else if (type == END_TIME) {
            return getEventEndCalendar(firstDay, detailEntity);
        } else {
            throw new IllegalArgumentException("Invalid output type defined");
        }
    }

    /**
     * @param firstDay     Calendar represents the first day of the school term
     * @param detailEntity DetailEntity to convert
     * @return Calendar object representing the start of the DetailEntity after conversion
     */
    private static Calendar getEventStartCalendar(@NonNull Calendar firstDay, @NonNull DetailEntity detailEntity) {

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
    private static Calendar getEventEndCalendar(@NonNull Calendar firstDay, @NonNull DetailEntity detailEntity) {

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

    public static List<Calendar> parseExamTime(@NonNull Exam exam) {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(getExamStartCalendar(exam));
        calendarList.add(getExamEndCalendar(exam.getDuration(), (Calendar) calendarList.get(0).clone()));
        return calendarList;
    }

    private static Calendar getExamStartCalendar(Exam exam) {
        Calendar calendar = Calendar.getInstance();

        String startStr = TimeConventionParser.to24hString(exam.getTime());

        String startTimeStrHour = startStr.substring(0, 2);
        String startTimeStrMin = startStr.substring(2);

        String[] arr = exam.getDate().trim().split(" ");

        // Set Time
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeStrHour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(startTimeStrMin));
        calendar.set(Calendar.SECOND, 0);

        // Set Year
        calendar.set(Calendar.YEAR, Integer.parseInt(arr[2].trim()));

        // Set Month
        calendar.set(Calendar.MONTH, MonthOfYear.valueOf(arr[1]).getValue());

        // Set date
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[0].trim()));

        return calendar;
    }


    private static Calendar getExamEndCalendar(float duration, Calendar calendar) {

        if (duration % 1 == 0) {
            calendar.add(Calendar.HOUR_OF_DAY, (int) duration);
        } else {
            double hourLength = Math.floor(duration);
            calendar.add(Calendar.HOUR_OF_DAY, (int) hourLength);
            double minuteLength = duration % 1;
            calendar.add(Calendar.MINUTE, (int) (minuteLength * 60));
        }
        return calendar;
    }

}
