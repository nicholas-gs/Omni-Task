package com.example.ntu_timetable_calendar.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ntu_timetable_calendar.Entity.TaskEntity;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void insert(TaskEntity taskEntity);

    @Update
    void update(TaskEntity taskEntity);

    @Delete
    void delete(TaskEntity taskEntity);

    @Query("SELECT * FROM task_table")
    LiveData<List<TaskEntity>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE timetableId = :timetableId")
    LiveData<List<TaskEntity>> getTimetableTasks(int timetableId);

    @Query("SELECT * FROM task_table WHERE courseEventEntityId = :courseEventEntityId")
    LiveData<List<TaskEntity>> getClassTasks(int courseEventEntityId);

    @Query("SELECT * FROM task_table WHERE id = :id LIMIT 1")
    LiveData<TaskEntity> getTask(int id);
}