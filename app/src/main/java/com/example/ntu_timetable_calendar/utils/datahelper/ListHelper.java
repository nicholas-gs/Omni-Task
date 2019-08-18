package com.example.ntu_timetable_calendar.utils.datahelper;

import com.example.ntu_timetable_calendar.models.jsonmodels.Course;
import com.example.ntu_timetable_calendar.models.jsonmodels.Detail;
import com.example.ntu_timetable_calendar.models.jsonmodels.Index;
import com.example.ntu_timetable_calendar.models.jsonmodels.Time;

import java.util.ArrayList;
import java.util.List;

public class ListHelper {

    private int flag = 0;

    /**
     * Insert dummy course object at index 0 into course List. We increment the value of flag every time
     * insertDummyCourse() is called, in order for diffUtil to trigger that first course POJO is different everytime
     * a new list is inserted. Without it, diffutil will see that the course object at index 0 is always the same,
     * hence the results textview will not be updated with the actual number of results.
     *
     * @param position Position to insert a dummy Course object in the courseList
     * @param courseList List of courses to insert a dummy object
     */
    public void insertDummyCourse(int position, List<Course> courseList) {
        List<Index> indexList = new ArrayList<>();
        List<Detail> details = new ArrayList<>();
        Detail detail = new Detail("xxx", "xxx", "xxx", new Time("xxx", "xxx",
                "xxx", 0), "xxx", flag, "xxx");
        details.add(detail);
        indexList.add(new Index("xxx", details));
        courseList.add(position, new Course("xxx", "xxx", "xxx", indexList));

        flag += 1;
    }

}
