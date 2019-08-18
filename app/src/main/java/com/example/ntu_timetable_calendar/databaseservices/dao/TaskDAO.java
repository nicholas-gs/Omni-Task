package com.example.ntu_timetable_calendar.databaseservices.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ntu_timetable_calendar.models.entities.TaskEntity;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    Long insert(TaskEntity taskEntity);

    @Update
    void update(TaskEntity taskEntity);

    @Delete
    void delete(TaskEntity taskEntity);

    @Query("SELECT * FROM task_table")
    LiveData<List<TaskEntity>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE timetableId = :timetableId ORDER BY deadLine ASC")
    LiveData<List<TaskEntity>> getTimetableTasks(int timetableId);

    @Query("SELECT * FROM task_table WHERE courseEventEntityId = :courseEventEntityId ORDER BY deadLine ASC")
    LiveData<List<TaskEntity>> getClassTasks(int courseEventEntityId);

    @Query("SELECT * FROM task_table WHERE id = :id LIMIT 1")
    LiveData<TaskEntity> getTask(int id);

    @Query("SELECT * FROM task_table WHERE deadLine BETWEEN :nowTime AND :deadLineTime ORDER BY deadLine ASC")
    LiveData<List<TaskEntity>> getTasksWithinTime(long nowTime, long deadLineTime);

    @Query("UPDATE task_table SET courseEventEntityId = -1")
    void clearAllClassesInTasks();

    @Query("UPDATE task_table SET isDone = 1 WHERE id = :taskId")
    void completeTask(int taskId);

    @Query("SELECT * FROM task_table WHERE isDone == 0")
    List<TaskEntity> getAllNotDoneTasks();
}