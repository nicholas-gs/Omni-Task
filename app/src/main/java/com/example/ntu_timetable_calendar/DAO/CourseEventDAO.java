package com.example.ntu_timetable_calendar.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;

import java.util.List;

@Dao
public interface CourseEventDAO {

    @Insert
    void insert(List<CourseEventEntity> courseEventEntity);

    @Update
    void update(CourseEventEntity courseEventEntity);

    @Delete
    void delete(CourseEventEntity courseEventEntity);

    @Query("SELECT * FROM course_event_table WHERE id = :id LIMIT 1")
    LiveData<CourseEventEntity> getCourseEvent(int id);

    @Query("SELECT * FROM course_event_table")
    LiveData<List<CourseEventEntity>> getAllCourseEvents();

    @Query("SELECT * FROM course_event_table WHERE timeTableId = :timetableId")
    LiveData<List<CourseEventEntity>> getTimetableCourseEvents(int timetableId);
}
