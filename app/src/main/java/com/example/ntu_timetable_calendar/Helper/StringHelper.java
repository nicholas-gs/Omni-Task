package com.example.ntu_timetable_calendar.Helper;

import androidx.annotation.NonNull;

import com.example.ntu_timetable_calendar.JsonModels.Course;
import com.example.ntu_timetable_calendar.JsonModels.Index;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static String ClassTimingParser(@NonNull Calendar startTimeCal, @NonNull Calendar endTimeCal) {
        Date startTimeDate = startTimeCal.getTime();
        Date endTimeDate = endTimeCal.getTime();

        return DateFormat.getDateInstance().format(startTimeDate) + ", " + DateFormat.getTimeInstance(DateFormat.SHORT).format(startTimeDate)
                + " - " + DateFormat.getTimeInstance(DateFormat.SHORT).format(endTimeDate);
    }
}
