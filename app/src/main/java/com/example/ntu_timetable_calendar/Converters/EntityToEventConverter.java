package com.example.ntu_timetable_calendar.Converters;

import android.graphics.Color;
import android.util.Log;

import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.DetailEntity;
import com.example.ntu_timetable_calendar.EventModel.Event;
import com.example.ntu_timetable_calendar.Helper.DayOfWeek;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Used to convert a list of CourseEntity from Room into a list of Event for display in a WeekView widget.
 * Used by TimetableDetailFragment
 */
public abstract class EntityToEventConverter {
    private static final String TAG = "EntityToEventConverterTAG";

    public static List<Event> convertEntitiesToEvents(List<CourseEntity> courseEntityList) {
        Log.d(TAG, "convertEntitiesToEvents: Size of courseEntityList - " + courseEntityList.size());
        Calendar calendar = Calendar.getInstance();
        List<Event> eventList = new ArrayList<>();

        int[] colors = colors();

        for (CourseEntity courseEntity : courseEntityList) {

            DetailEntity detailEntity = courseEntity.getDetailEntity();

            // Sometimes it is a online course, so there is no time, and no need to show on the WeekView widget
            if (detailEntity.getTime().getStart().length() == 0) {
                continue;
            }

            Calendar startTime = (Calendar) calendar.clone();
            Calendar endTime = (Calendar) startTime.clone();

            String startTimeStrHour = detailEntity.getTime().getStart().substring(0, 2);
            String startTimeStrMin = detailEntity.getTime().getStart().substring(2, 4);
            String endTimeStrHour = detailEntity.getTime().getEnd().substring(0, 2);
            String endTimeStrMin = detailEntity.getTime().getEnd().substring(2, 4);

            // Start time
            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeStrHour));
            startTime.set(Calendar.MINUTE, Integer.parseInt(startTimeStrMin));
            startTime.set(Calendar.SECOND, 0);
            endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeStrHour));
            endTime.set(Calendar.MINUTE, Integer.parseInt(endTimeStrMin));
            endTime.set(Calendar.SECOND, 0);

            // Set day
            startTime.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(detailEntity.getDay()).getValue());
            endTime.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(detailEntity.getDay()).getValue());

            // SetMonth
            startTime.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            endTime.set(Calendar.MONTH, calendar.get(Calendar.MONTH));

            // Set Year
            startTime.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            endTime.set(Calendar.YEAR, calendar.get(Calendar.YEAR));

            // Title
            String title = courseEntity.getCourseCode() + " " + detailEntity.getType() + " " + detailEntity.getRemarks();
            Long id = Long.parseLong(courseEntity.getIndexNumber());
            Event event = new Event(id, title, startTime, endTime, detailEntity.getLocation(), colors[(id.intValue()) % 4],
                    false, false);
            eventList.add(event);
        }
        return eventList;
    }

    private static int[] colors() {
        int pink = Color.parseColor("#f57f68");
        int green = Color.parseColor("#87d288");
        int yellow = Color.parseColor("#f8b552");
        int blue = Color.parseColor("#59dbe0");

        return new int[]{
                pink, green, yellow, blue
        };
    }
}