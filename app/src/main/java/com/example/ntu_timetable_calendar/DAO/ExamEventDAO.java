package com.example.ntu_timetable_calendar.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ntu_timetable_calendar.Entity.ExamEventEntity;

import java.util.List;

@Dao
public interface ExamEventDAO {

    @Insert
    void insert(List<ExamEventEntity> examEventEntityList);

    @Update
    void update(ExamEventEntity examEventEntity);

    @Delete
    void delete(ExamEventEntity examEventEntity);

    @Query("SELECT * FROM exam_event_table")
    LiveData<List<ExamEventEntity>> getAllExamEvents();

    @Query("SELECT * FROM exam_event_table WHERE timeTableId = :timetableId")
    LiveData<List<ExamEventEntity>> getTimetableExamEvents(int timetableId);
}
