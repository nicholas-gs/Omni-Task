package com.example.ntu_timetable_calendar.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "time_table")
public class TimeEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String full;
    private String start;
    private String end;
    private float duration;

    public TimeEntity(String full, String start, String end, float duration) {
        this.full = full;
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    // Getters

    public int getId() {
        return id;
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
