package com.example.ntu_timetable_calendar.Entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "timetable_table", indices = {@Index(value = {"id"}, unique = true)})
public class TimetableEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private boolean isMainTimetable;

    public TimetableEntity(String name, String description, boolean isMainTimetable) {
        this.name = name;
        this.description = description;
        this.isMainTimetable = isMainTimetable;
    }

    // Setter

    public void setId(int timetableId) {
        this.id = timetableId;
    }


    // Getters

    public int getId() {
        return id;
    }

    public boolean getIsMainTimetable() {
        return isMainTimetable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
