package com.example.ntu_timetable_calendar.Converters;

import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;
import com.example.ntu_timetable_calendar.Helper.CalendarParser;
import com.example.ntu_timetable_calendar.Helper.EventColors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Take in a list of CourseEntity and converts them into a list of CourseEventEntity for storage in Room.
 */
public class CourseEventToCourseEventEntityConverter {

    private List<CourseEntity> courseEntityList;
    private List<CourseEventEntity> finalList = new ArrayList<>();
    private int startMonth;
    private int startDate;
    private int startYear;

    public CourseEventToCourseEventEntityConverter(List<CourseEntity> courseEntityList, int startYear, int startMonth, int startDate) {
        this.courseEntityList = courseEntityList;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDate = startDate;
    }

    public List<CourseEventEntity> convert() {

        Calendar firstDay = Calendar.getInstance();

        // Set year
        firstDay.set(Calendar.YEAR, startYear);

        // Set month
        firstDay.set(Calendar.MONTH, startMonth);

        // Set date
        firstDay.set(Calendar.DAY_OF_MONTH, startDate);

        // For whatever reason, we have to call .get() in order for the calendar object to internalize the changes made to it!
        firstDay.get(Calendar.DAY_OF_MONTH);

        for (CourseEntity courseEntity : courseEntityList) {

            // Sometimes it is a online course, so there is no time, and no need to show on the WeekView widget and save it in Room
            if (courseEntity.getDetailEntity().getTime().getStart().length() == 0) {
                continue;
            }

            Calendar startTime = CalendarParser.parseEventTime(CalendarParser.START_TIME, firstDay, courseEntity.getDetailEntity());
            Calendar endTime = CalendarParser.parseEventTime(CalendarParser.END_TIME, firstDay, courseEntity.getDetailEntity());

            String title = courseEntity.getCourseCode() + " " + courseEntity.getDetailEntity().getType() + " " + courseEntity.getDetailEntity().getRemarks();
            CourseEventEntity courseEventEntity = new CourseEventEntity(courseEntity.getTimeTableId(), title, courseEntity.getDetailEntity().getLocation(),
                    EventColors.colors()[Integer.valueOf(courseEntity.getIndexNumber()) % 4], false, false, startTime.getTimeInMillis(), endTime.getTimeInMillis());

            this.finalList.add(courseEventEntity);

            for (int i = 1; i <= 13; i++) {

                startTime.add(Calendar.DAY_OF_MONTH, 7);
                startTime.get(Calendar.DAY_OF_MONTH);
                endTime.add(Calendar.DAY_OF_MONTH, 7);
                endTime.get(Calendar.DAY_OF_MONTH);

                CourseEventEntity tempCourseEventEntity = new CourseEventEntity(courseEntity.getTimeTableId(), title, courseEntity.getDetailEntity().getLocation(),
                        EventColors.colors()[Integer.valueOf(courseEntity.getIndexNumber()) % 4], false, false, startTime.getTimeInMillis(), endTime.getTimeInMillis());

                if (i != 7) {
                    this.finalList.add(tempCourseEventEntity);
                }
            }
        }

        return this.finalList;
    }

}
