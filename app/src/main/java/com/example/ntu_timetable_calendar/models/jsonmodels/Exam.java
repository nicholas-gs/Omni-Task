package com.example.ntu_timetable_calendar.models.jsonmodels;

public class Exam {
    private String date;
    private String day;
    private String time;
    private String code;
    private String name;
    private float duration;

    public Exam(String date, String day, String time, String code, String name, float duration) {
        this.date = date;
        this.day = day;
        this.time = time;
        this.code = code;
        this.name = name;
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public float getDuration() {
        return duration;
    }
}
