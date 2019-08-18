package com.example.ntu_timetable_calendar.models.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "course_event_table", foreignKeys = @ForeignKey(entity = TimetableEntity.class, parentColumns = "id",
        childColumns = "timeTableId", onDelete = CASCADE))
public class CourseEventEntity {

    @ColumnInfo(index = true)
    private int timeTableId; // Id of the timetable this course belongs to is stored here

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String location;

    private int color;

    private Boolean isAllDay;

    private Boolean isCanceled;

    private long startTime;

    private long endTime;

    public CourseEventEntity(int timeTableId, String title, String location, int color, Boolean isAllDay, Boolean isCanceled, long startTime, long endTime) {
        this.timeTableId = timeTableId;
        this.title = title;
        this.location = location;
        this.color = color;
        this.isAllDay = isAllDay;
        this.isCanceled = isCanceled;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    // Getters

    public int getTimeTableId() {
        return timeTableId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public int getColor() {
        return color;
    }

    public Boolean getAllDay() {
        return isAllDay;
    }

    public Boolean getCanceled() {
        return isCanceled;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

}
