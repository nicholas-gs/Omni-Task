package com.example.ntu_timetable_calendar.JsonModels;

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

    public void setFull(String full) {
        this.full = full;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
