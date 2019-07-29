package com.example.ntu_timetable_calendar.Converters;

import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.DetailEntity;
import com.example.ntu_timetable_calendar.EventModel.Event;
import com.example.ntu_timetable_calendar.Helper.CalendarParser;
import com.example.ntu_timetable_calendar.Helper.EventColors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Used to convert a list of CourseEntity from Room into a list of Event for display in a WeekView widget.
 * Used by TimetableDetailFragment
 */
public abstract class CourseEntityToEventConverter {

    public static List<Event> convertEntitiesToEvents(List<CourseEntity> courseEntityList) {

        Calendar calendar = Calendar.getInstance();
        List<Event> eventList = new ArrayList<>();

        for (CourseEntity courseEntity : courseEntityList) {

            DetailEntity detailEntity = courseEntity.getDetailEntity();

            // Sometimes it is a online course, so there is no time, and no need to show on the WeekView widget
            if (detailEntity.getTime().getStart().length() == 0) {
                continue;
            }

            Calendar startTime = CalendarParser.parseEventTime(CalendarParser.START_TIME, calendar, detailEntity);
            Calendar endTime = CalendarParser.parseEventTime(CalendarParser.END_TIME, calendar, detailEntity);

            // Title
            String title = courseEntity.getCourseCode() + " " + detailEntity.getType() + " " + detailEntity.getRemarks();
            Long id = Long.parseLong(courseEntity.getIndexNumber());
            Event event = new Event(id, title, startTime, endTime, detailEntity.getLocation(), EventColors.colors()[(id.intValue()) % 4],
                    false, false);
            eventList.add(event);
        }
        return eventList;
    }

}
