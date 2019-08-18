package com.example.ntu_timetable_calendar.databaseservices.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ntu_timetable_calendar.models.entities.AlarmEntity;

import java.util.List;

@Dao
public interface AlarmDAO {

    @Insert
    Long insert(AlarmEntity alarmEntity);

    @Update
    void update(AlarmEntity alarmEntity);

    @Delete
    void delete(AlarmEntity alarmEntity);

    @Query("DELETE FROM alarm_table")
    void deleteAllAlarms();

    @Query("SELECT * FROM alarm_table WHERE taskId = :taskId")
    LiveData<List<AlarmEntity>> getAllTaskAlarms(int taskId);

    @Query("DELETE FROM alarm_table WHERE taskId =:taskId")
    void deleteTaskAlarms(int taskId);

    @Query("SELECT * FROM alarm_table")
    LiveData<List<AlarmEntity>> getAllAlarms();

    @Query("SELECT * FROM alarm_table")
    List<AlarmEntity> getAllAlarmsList();
}