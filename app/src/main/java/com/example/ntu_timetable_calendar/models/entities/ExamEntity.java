package com.example.ntu_timetable_calendar.models.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "exam_table", foreignKeys = @ForeignKey(entity = TimetableEntity.class, parentColumns = "id",
        childColumns = "timeTableId", onDelete = CASCADE))
public class ExamEntity {

    @ColumnInfo(index = true)
    private int timeTableId; // Id of the timetable this course belongs to is stored here

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;
    private String day;
    private String time;
    private String code;
    private String name;
    private float duration;

    public ExamEntity(int timeTableId, String date, String day, String time, String code, String name, float duration) {
        this.timeTableId = timeTableId;
        this.date = date;
        this.day = day;
        this.time = time;
        this.code = code;
        this.name = name;
        this.duration = duration;
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
