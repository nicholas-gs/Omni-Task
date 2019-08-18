package com.example.ntu_timetable_calendar.converters.objectconverters;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.models.entities.CourseEntity;
import com.example.ntu_timetable_calendar.models.entities.DetailEntity;
import com.example.ntu_timetable_calendar.models.entities.TimeEntity;
import com.example.ntu_timetable_calendar.models.jsonmodels.Course;
import com.example.ntu_timetable_calendar.models.jsonmodels.Detail;
import com.example.ntu_timetable_calendar.models.jsonmodels.Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Convert a list of courses with the Map of selection index into a list of CourseEntity for Room to save
 * Used in SQLRepository
 */
public class CourseToCourseEntityConverter {

    private int timetableId;
    private List<Course> courseList;
    private Map<String, String> indexSel;

    public CourseToCourseEntityConverter(int timetableId, @NonNull List<Course> courseList, @NonNull Map<String, String> indexSel) {
        this.timetableId = timetableId;
        this.courseList = courseList;
        this.indexSel = indexSel;
    }

    public List<CourseEntity> convert() {
        List<CourseEntity> courseEntityList = new ArrayList<>();

        // Put the courses into a HashMap, with the course code as the key, to make it easier later.
        Map<String, Course> courseMap = new HashMap<>();
        for (Course course : courseList) {
            courseMap.put(course.getCourseCode(), course);
        }

        for (String courseCode : indexSel.keySet()) {
            String indexSelectionStr = indexSel.get(courseCode);
            Course course = courseMap.get(courseCode);

            if (course != null) {
                for (Index index : course.getIndexes()) {
                    if (index.getIndexNumber().equals(indexSelectionStr)) {
                        List<Detail> detailList = index.getDetails();

                        for (Detail detail : detailList) {
                            CourseEntity courseEntity = new CourseEntity(timetableId, course.getCourseCode(),
                                    course.getName(), course.getAu(), indexSelectionStr, new DetailEntity(
                                    detail.getType(), detail.getGroup(), detail.getDay(), detail.getLocation(),
                                    detail.getFlag(), detail.getRemarks(), new TimeEntity(detail.getTime().getFull(),
                                    detail.getTime().getStart(), detail.getTime().getEnd(), detail.getTime().getDuration())
                            ));

                            courseEntityList.add(courseEntity);
                        }
                    }
                }
            }
        }

        return courseEntityList;
    }
}
