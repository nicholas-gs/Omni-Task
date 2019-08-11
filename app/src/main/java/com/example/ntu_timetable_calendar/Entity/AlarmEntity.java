package com.example.ntu_timetable_calendar.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "alarm_table", foreignKeys = @ForeignKey(entity = TaskEntity.class, parentColumns = "id",
        childColumns = "taskId", onDelete = CASCADE))
public class AlarmEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(index = true)
    private int taskId; // ID of the task this AlarmEntity belongs to

    private String title;
    private long time;

    public AlarmEntity(int taskId, long time, String title) {
        this.taskId = taskId;
        this.time = time;
        this.title = title;
    }

    // Getters

    public int getId() {
        return id;
    }

    public int getTaskId() {
        return taskId;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
