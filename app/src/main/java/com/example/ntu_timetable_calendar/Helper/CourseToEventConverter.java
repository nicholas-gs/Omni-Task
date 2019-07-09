package com.example.ntu_timetable_calendar.Helper;

import android.graphics.Color;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.CourseModels.Detail;
import com.example.ntu_timetable_calendar.CourseModels.Index;
import com.example.ntu_timetable_calendar.EventModel.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CourseToEventConverter {

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


    public static List<Event> covertCourseToEvent(List<Course> courseList, Map<String, String> indexList) {

        int pink = Color.parseColor("#f57f68");
        int green = Color.parseColor("#87d288");
        int yellow = Color.parseColor("#f8b552");
        int blue = Color.parseColor("#59dbe0");

        int colors[] = new int[]{
                pink, green, yellow, blue
        };

        int i = 1;

        Calendar calendar = Calendar.getInstance();
        List<Event> eventList = new ArrayList<>();

        Map<String, Course> courseMap = new HashMap<>();
        for (Course course : courseList) {
            courseMap.put(course.getCourseCode(), course);
        }

        for (String courseCode : indexList.keySet()) {
            String indexSelectionStr = indexList.get(courseCode);
            Course course = courseMap.get(courseCode);

            for (Index index : course.getIndexes()) {
                if (index.getIndexNumber().equals(indexSelectionStr)) {
                    List<Detail> detailList = index.getDetails();

                    for (Detail detail : detailList) {
                        Calendar startTime = (Calendar) calendar.clone();
                        Calendar endTime = (Calendar) startTime.clone();

                        // Sometimes it is a online course, so there is no time
                        if(detail.getTime().getStart().length() == 0){
                            break;
                        }
                        String startTimeStrHour = detail.getTime().getStart().substring(0, 2);
                        String startTimeStrMin = detail.getTime().getStart().substring(2, 4);
                        String endTimeStrHour = detail.getTime().getEnd().substring(0, 2);
                        String endTimeStrMin = detail.getTime().getEnd().substring(2, 4);

                        // Start time
                        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeStrHour));
                        startTime.set(Calendar.MINUTE, Integer.parseInt(startTimeStrMin));
                        endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeStrHour));
                        endTime.set(Calendar.MINUTE, Integer.parseInt(endTimeStrMin));

                        // Set day
                        startTime.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(detail.getDay()).getValue());
                        endTime.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(detail.getDay()).getValue());

                        String title = courseCode + " " + detail.getRemarks();
                        Event event = new Event(Long.parseLong(index.getIndexNumber()), title, startTime, endTime, detail.getLocation(),
                                colors[i%4], false, false);
                        eventList.add(event);
                    }

                    break;
                }
            }
            i += 1;
        }

        return eventList;
    }

}
