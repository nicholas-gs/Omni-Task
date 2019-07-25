package com.example.ntu_timetable_calendar.Helper;

import android.graphics.Color;

public abstract class EventColors {

    public static int[] colors() {
        int pink = Color.parseColor("#f57f68");
        int green = Color.parseColor("#87d288");
        int yellow = Color.parseColor("#f8b552");
        int blue = Color.parseColor("#59dbe0");

        return new int[]{
                pink, green, yellow, blue
        };
    }


}

