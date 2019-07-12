package com.example.ntu_timetable_calendar.Entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "detail_table")
public class DetailEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String group;
    private String day;
    private String location;
    private int flag;
    private String remarks;
    @Embedded(prefix = "time_")
    private TimeEntity time;

    public DetailEntity(String type, String group, String day, String location,
                        int flag, String remarks, TimeEntity time) {
        this.type = type;
        this.group = group;
        this.day = day;
        this.location = location;
        this.flag = flag;
        this.remarks = remarks;
        this.time = time;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    // Getters

    public int getId() {
        return id;
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

    public String getLocation() {
        return location;
    }

    public int getFlag() {
        return flag;
    }

    public String getRemarks() {
        return remarks;
    }

    public TimeEntity getTime() {
        return time;
    }

}
