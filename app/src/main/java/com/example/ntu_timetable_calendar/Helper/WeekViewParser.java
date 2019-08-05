package com.example.ntu_timetable_calendar.Helper;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;
import com.example.ntu_timetable_calendar.Entity.ExamEventEntity;
import com.example.ntu_timetable_calendar.EventModel.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Helper class for converting objects from Room into Events for displays
 * Currently used by HomeFragment and ChooseClassFragment
 */
public abstract class WeekViewParser {

    /**
     * Takes in a list of CourseEventEntity from Room and converts them into Event for display in the WeekView widget
     *
     * @param courseEventEntityList List of CourseEventEntity from Room
     */
    public static List<Event> saveCourseEventEntityList(@NonNull List<CourseEventEntity> courseEventEntityList) {

        Calendar calendar = Calendar.getInstance();

        List<Event> eventList = new ArrayList<>();

        for (CourseEventEntity courseEventEntity : courseEventEntityList) {
            Calendar start = (Calendar) calendar.clone();
            start.setTimeInMillis(courseEventEntity.getStartTime());
            Calendar end = (Calendar) calendar.clone();
            end.setTimeInMillis(courseEventEntity.getEndTime());

            // Id of the event is also the id of the original courseEventEntity
            Event event = new Event(courseEventEntity.getId(), courseEventEntity.getTitle(), start, end
                    , courseEventEntity.getLocation(), courseEventEntity.getColor(),
                    courseEventEntity.getAllDay(), courseEventEntity.getCanceled());

            eventList.add(event);
        }

        return eventList;
    }


    /**
     * Takes in a list of ExamEventEntity from Room and converts them into Event for display in the WeekView widget
     *
     * @param examEventEntityList List of ExamEventEntity from Room
     */
    public static List<Event> saveExamEventEntityList(@NonNull List<ExamEventEntity> examEventEntityList) {

        Calendar calendar = Calendar.getInstance();

        List<Event> eventList = new ArrayList<>();

        for (ExamEventEntity examEventEntity : examEventEntityList) {
            Calendar start = (Calendar) calendar.clone();
            start.setTimeInMillis(examEventEntity.getStartTime());
            Calendar end = (Calendar) calendar.clone();
            end.setTimeInMillis(examEventEntity.getEndTime());

            Event event = new Event(examEventEntity.getId(), examEventEntity.getTitle(), start, end, examEventEntity.getLocation(), examEventEntity.getColor(),
                    examEventEntity.getAllDay(), examEventEntity.getCanceled());

            eventList.add(event);
        }

        return eventList;
    }
}