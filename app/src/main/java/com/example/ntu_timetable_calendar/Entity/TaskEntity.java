package com.example.ntu_timetable_calendar.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.ntu_timetable_calendar.TypeConverter.AlarmTimingChosenConverter;
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

    @TypeConverters(AlarmTimingChosenConverter.class)
    private boolean[] alarmTimingChosen;

    public TaskEntity(int timetableId, int courseEventEntityId, String title, String description, Long deadLine, int priorityLevel, List<Long> alarmList,
                      boolean[] alarmTimingChosen) {
        this.timetableId = timetableId;
        this.courseEventEntityId = courseEventEntityId;
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.priorityLevel = priorityLevel;
        this.alarmList = alarmList;
        this.alarmTimingChosen = alarmTimingChosen;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
    }

    public void setCourseEventEntityId(int courseEventEntityId) {
        this.courseEventEntityId = courseEventEntityId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadLine(Long deadLine) {
        this.deadLine = deadLine;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public void setAlarmList(List<Long> alarmList) {
        this.alarmList = alarmList;
    }

    public void setAlarmTimingChosen(boolean[] alarmTimingChosen) {
        this.alarmTimingChosen = alarmTimingChosen;
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

    public boolean[] getAlarmTimingChosen() {
        return alarmTimingChosen;
    }
}