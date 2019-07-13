package com.example.ntu_timetable_calendar.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Each CourseEntity represents a one hour block of the timetable that the user has saved
 */
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "course_table", foreignKeys = @ForeignKey(entity = TimetableEntity.class, parentColumns = "id",
        childColumns = "timeTableId", onDelete = CASCADE))
public class CourseEntity {

    @ColumnInfo(index = true)
    private int timeTableId; // Id of the timetable this course belongs to is stored here
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String courseCode;
    private String indexNumber;
    private String name;
    private String au;

    @Embedded(prefix = "detail_")
    private DetailEntity detailEntity;

    public CourseEntity(int timeTableId, String courseCode, String name,
                        String au, String indexNumber, DetailEntity detailEntity) {
        this.timeTableId = timeTableId;
        this.courseCode = courseCode;
        this.name = name;
        this.au = au;
        this.indexNumber = indexNumber;
        this.detailEntity = detailEntity;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    // Getters

    public int getId() {
        return id;
    }

    public int getTimeTableId() {
        return timeTableId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public String getAu() {
        return au;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public DetailEntity getDetailEntity() {
        return detailEntity;
    }
}
