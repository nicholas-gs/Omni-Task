package com.example.ntu_timetable_calendar.CourseModels;

public class Time {
    private String full;
    private String start;
    private String end;
    private float duration;

    public Time(String full, String start, String end, int duration) {
        this.full = full;
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

    public String getFull() {
        return full;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public float getDuration() {
        return duration;
    }
}
