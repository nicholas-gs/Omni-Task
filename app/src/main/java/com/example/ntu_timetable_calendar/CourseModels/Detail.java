package com.example.ntu_timetable_calendar.CourseModels;

public class Detail {

    private String type;
    private String group;
    private String day;
    private Time time;
    private String location;
    private int flag;
    private String remarks;

    public Detail(String type, String group, String day, Time time, String location, int flag, String remarks) {
        this.type = type;
        this.group = group;
        this.day = day;
        this.time = time;
        this.location = location;
        this.flag = flag;
        this.remarks = remarks;
    }

    public String getType() {
        return type;
    }

    public String getGroup() {
        return group;
    }

    public String getDay() {
        return day;
    }

    public Time getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public int getFlag() {
        return flag;
    }

    public String getRemarks() {
        return remarks;
    }
}
