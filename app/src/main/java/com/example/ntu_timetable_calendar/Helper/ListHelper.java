package com.example.ntu_timetable_calendar.Helper;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.CourseModels.Detail;
import com.example.ntu_timetable_calendar.CourseModels.Index;
import com.example.ntu_timetable_calendar.CourseModels.Time;

import java.util.ArrayList;
import java.util.List;

public abstract class ListHelper {

    /**
     * Insert dummy course object into course List
     * @param position
     * @param courseList
     */
    public static void insertDummyCourse(int position, List<Course> courseList){
        List<Index> indexList = new ArrayList<>();
        List<Detail> details = new ArrayList<>();
        Detail detail = new Detail("xxx", "xxx", "xxx", new Time("xxx", "xxx",
                "xxx", 0), "xxx", 0, "xxx");
        details.add(detail);
        indexList.add(new Index("xxx", details));
        courseList.add(position, new Course("xxx", "xxx", "xxx", indexList));
    }

}
