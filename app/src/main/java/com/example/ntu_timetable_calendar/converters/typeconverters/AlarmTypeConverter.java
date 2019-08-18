package com.example.ntu_timetable_calendar.converters.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Type converter for converter a list of long to and from Json format.
 * Currently used in TaskEntity
 */
public class AlarmTypeConverter {

    @TypeConverter
    public static String listToString(List<Long> listOfAlarms) {
        Gson gson = new Gson();
        return gson.toJson(listOfAlarms);
    }

    @TypeConverter
    public static List<Long> StringToList(String alarmString) {
        if (alarmString == null || alarmString.equals("")) {
            return new ArrayList<>();
        } else {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Long>>(){}.getType();
            return gson.fromJson(alarmString,listType);
        }
    }
}