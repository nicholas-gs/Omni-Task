package com.example.ntu_timetable_calendar.databaseservices.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ntu_timetable_calendar.models.entities.ExamEntity;

import java.util.List;

@Dao
public interface ExamDAO {

    @Insert
    void insert(List<ExamEntity> examEntityList);

    @Update
    void update(ExamEntity examEntity);

    @Delete
    void delete(ExamEntity examEntity);

    @Query("SELECT * FROM exam_table")
    LiveData<List<ExamEntity>> getAllExams();

    @Query("SELECT * FROM exam_table WHERE timeTableId = :timetableId")
    LiveData<List<ExamEntity>> getTimetableExams(int timetableId);
}
