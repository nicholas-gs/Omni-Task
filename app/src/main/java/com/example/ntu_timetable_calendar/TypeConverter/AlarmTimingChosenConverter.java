package com.example.ntu_timetable_calendar.TypeConverter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AlarmTimingChosenConverter {

    @TypeConverter
    public static String ArrayToString(boolean[] alarmTimingChosen) {
        Gson gson = new Gson();
        return gson.toJson(alarmTimingChosen);
    }

    @TypeConverter
    public static boolean[] StringToArray(String json) {
        if (json != null) {
            Gson gson = new Gson();
            Type arrayType = new TypeToken<boolean[]>() {
            }.getType();
            return gson.fromJson(json, arrayType);
        } else {
            return new boolean[5];
        }
    }
}