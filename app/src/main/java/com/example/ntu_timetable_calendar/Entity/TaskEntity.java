package com.example.ntu_timetable_calendar.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.ntu_timetable_calendar.TypeConverter.AlarmTypeConverter;

import java.util.List;

@Entity(tableName = "task_table")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int timetableId;

    private int courseEventEntityId;

    private String title;
    private String description;
    private Long deadLine;
    private int priorityLevel;

    @TypeConverters(AlarmTypeConverter.class)
    private List<Long> alarmList;

    public TaskEntity(int timetableId, int courseEventEntityId, String title, String description, Long deadLine, int priorityLevel, List<Long> alarmList) {
        this.timetableId = timetableId;
        this.courseEventEntityId = courseEventEntityId;
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.priorityLevel = priorityLevel;
        this.alarmList = alarmList;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    // Getters

    public int getId() {
        return id;
    }

    public int getTimetableId() {
        return timetableId;
    }

    public int getCourseEventEntityId() {
        return courseEventEntityId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getDeadLine() {
        return deadLine;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public List<Long> getAlarmList() {
        return alarmList;
    }
}