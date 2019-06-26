package com.example.ntu_timetable_calendar.Helper;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.CourseModels.Index;

public abstract class StringHelper {

    public static String formatIndexString(Course course) {
        StringBuilder indexStrBuilder = new StringBuilder("Index : ");
        for (Index index : course.getIndexes()) {
            indexStrBuilder.append(index.getIndexNumber()).append(", ");
        }
        String indexStr = indexStrBuilder.toString();
        if (indexStr.length() > 0 && indexStr.charAt(indexStr.length() - 2) == ',') {
            indexStr = indexStr.substring(0, indexStr.length() - 2);
        }
        return indexStr;
    }

    public static String formatNameString(String name) {
        if (name != null && name.length() > 0 && name.charAt(name.length() - 1) == '*') {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

}
