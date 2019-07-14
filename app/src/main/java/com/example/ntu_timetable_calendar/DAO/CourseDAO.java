package com.example.ntu_timetable_calendar.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ntu_timetable_calendar.Entity.CourseEntity;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CourseEntity> courseEntityList);

    @Update
    void update(CourseEntity courseEntity);

    @Delete
    void delete(CourseEntity courseEntity);

    @Query("SELECT * FROM course_table")
    LiveData<List<CourseEntity>> getAllCourses();

    @Query("SELECT * FROM course_table WHERE timeTableId = :timetableId")
    LiveData<List<CourseEntity>> getTimetableCourses(int timetableId);
}
