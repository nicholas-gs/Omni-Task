package com.example.ntu_timetable_calendar.Converters;

import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;
import com.example.ntu_timetable_calendar.Helper.DayOfWeek;
import com.example.ntu_timetable_calendar.Helper.EventColors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Take in a list of CourseEntity and converts them into a list of CourseEventEntity for storage in Room.
 */
public class CourseEventToCourseEventEntityConverter {

    private List<CourseEntity> courseEntityList;
    private List<CourseEventEntity> courseEventEntityList = new ArrayList<>();
    private int startMonth;
    private int startDate;

    public CourseEventToCourseEventEntityConverter(List<CourseEntity> courseEntityList, int startMonth, int startDate) {
        this.courseEntityList = courseEntityList;
        this.startMonth = startMonth;
        this.startDate = startDate;
    }

    public List<CourseEventEntity> convert() {

        Calendar calendar = Calendar.getInstance();
        Calendar firstDay = (Calendar) calendar.clone();

        // Set month
        firstDay.set(Calendar.MONTH, startMonth);

        // Set week
        firstDay.set(Calendar.DAY_OF_MONTH, startDate);

        // For whatever reason, we have to call .get() in order for the calendar object to internalize the changes made to it!
        firstDay.get(Calendar.DAY_OF_MONTH);

        for (CourseEntity courseEntity : courseEntityList) {

            // Sometimes it is a online course, so there is no time, and no need to show on the WeekView widget and save it in Room
            if (courseEntity.getDetailEntity().getTime().getStart().length() == 0) {
                continue;
            }

            Calendar startTime = (Calendar) firstDay.clone();
            Calendar endTime = (Calendar) firstDay.clone();

            String startTimeStrHour = courseEntity.getDetailEntity().getTime().getStart().substring(0, 2);
            String startTimeStrMin = courseEntity.getDetailEntity().getTime().getStart().substring(2, 4);
            String endTimeStrHour = courseEntity.getDetailEntity().getTime().getEnd().substring(0, 2);
            String endTimeStrMin = courseEntity.getDetailEntity().getTime().getEnd().substring(2, 4);

            // Start time
            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeStrHour));
            startTime.set(Calendar.MINUTE, Integer.parseInt(startTimeStrMin));
            startTime.set(Calendar.SECOND, 0);
            endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimeStrHour));
            endTime.set(Calendar.MINUTE, Integer.parseInt(endTimeStrMin));
            endTime.set(Calendar.SECOND, 0);

            // Set day
            startTime.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(courseEntity.getDetailEntity().getDay()).getValue());
            endTime.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(courseEntity.getDetailEntity().getDay()).getValue());

            // For whatever reason, we have to call .get() in order for the calendar object to internalize the changes made to it!
            startTime.get(Calendar.DAY_OF_MONTH);

            String title = courseEntity.getCourseCode() + " " + courseEntity.getDetailEntity().getType() + " " + courseEntity.getDetailEntity().getRemarks();
            CourseEventEntity courseEventEntity = new CourseEventEntity(courseEntity.getTimeTableId(), title, courseEntity.getDetailEntity().getLocation(),
                    EventColors.colors()[Integer.valueOf(courseEntity.getIndexNumber()) % 4], false, false, startTime.getTimeInMillis(), endTime.getTimeInMillis());

            this.courseEventEntityList.add(courseEventEntity);
        }

        return this.courseEventEntityList;
    }

}
