package com.example.ntu_timetable_calendar.converters.objectconverters;

import com.example.ntu_timetable_calendar.models.Event;
import com.example.ntu_timetable_calendar.utils.constants.DayOfWeek;
import com.example.ntu_timetable_calendar.utils.constants.EventColors;
import com.example.ntu_timetable_calendar.models.jsonmodels.Course;
import com.example.ntu_timetable_calendar.models.jsonmodels.Detail;
import com.example.ntu_timetable_calendar.models.jsonmodels.Index;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract class and its method convertCourseToEvent(...) is used by the PlanFragment.
 */
public abstract class CourseToEventConverter {


    /**
     * Converts a list of courses, with their chosen index, into a list of events for display in a WeekView widget.
     *
     * @param courseList List of courses chosen by user
     * @param indexList  HashMap key - Course code, HashMap value - Chosen index
     * @return List of events for display in WeekView widget
     */
    public static List<Event> convertCourseToEvent(List<Course> courseList, Map<String, String> indexList) {

        // We iterate through a integer in order to give the events different colors
        int i = 1;

        Calendar calendar = Calendar.getInstance();
        List<Event> eventList = new ArrayList<>();

        // Put the courses into a HashMap, with the course code as the key, to make it easier later.
        Map<String, Course> courseMap = new HashMap<>();
        for (Course course : courseList) {
            courseMap.put(course.getCourseCode(), course);
        }

        for (String courseCode : indexList.keySet()) {
            String indexSelectionStr = indexList.get(courseCode);
            Course course = courseMap.get(courseCode);

            if (course != null) {
                for (Index index : course.getIndexes()) {
                    if (index.getIndexNumber().equals(indexSelectionStr)) {
                        List<Detail> detailList = index.getDetails();

                        for (Detail detail : detailList) {

                            // Sometimes it is a online course, so there is no time, and no need to show on the WeekView widget
                            if (detail.getTime().getStart().length() == 0) {
                                break;
                            }

                            eventList.add(convertDetailToEvent(detail, calendar, courseCode, index, i));
                        }

                        break;
                    }
                }
            }
            i += 1;
        }

        return eventList;
    }

    /**
     * Convert an individual Detail POJO into a Event POJO
     *
     * @param detail     Detail POJO to convert into a Event POJO
     * @param calendar   Calendar object
     * @param courseCode Course code for detail
     * @param index      Index that detail POJO belongs to
     * @param i          Counter for choosing a color for the event object
     * @return Return the new event created
     */
    private static Event convertDetailToEvent(Detail detail, Calendar calendar, String courseCode, Index index, int i) {

        Calendar startTime = (Calendar) calendar.clone();
        Calendar endTime = (Calendar) startTime.clone();

        String startTimeStrHour = detail.getTime().getStart().substring(0, 2);
        String startTimeStrMin = detail.getTime().getStart().substring(2, 4);
        String endTimeStrHour = detail.getTime().getEnd().substring(0, 2);
        String endTimeStrMin = detail.getTime().getEnd().substring(2, 4);

        // Start time
        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeStrHour));
        startTime.set(Calendar.MINUTE, Integer.parseInt(startTimeStrMin));
        startTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeStrHour));
        endTime.set(Calendar.MINUTE, Integer.parseInt(endTimeStrMin));
        endTime.set(Calendar.SECOND, 0);

        // Set day
        startTime.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(detail.getDay()).getValue());
        endTime.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(detail.getDay()).getValue());

        // SetMonth
        startTime.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        endTime.set(Calendar.MONTH, calendar.get(Calendar.MONTH));

        // Set Year
        startTime.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        endTime.set(Calendar.YEAR, calendar.get(Calendar.YEAR));

        String title = courseCode + " " + detail.getType() + " " + detail.getRemarks();

        return new Event(Long.parseLong(index.getIndexNumber()), title, startTime, endTime, detail.getLocation(),
                EventColors.colors()[i % 4], false, false);
    }

}
