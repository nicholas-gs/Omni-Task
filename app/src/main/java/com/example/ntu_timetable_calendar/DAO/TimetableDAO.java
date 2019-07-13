package com.example.ntu_timetable_calendar.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ntu_timetable_calendar.Entity.TimetableEntity;

import java.util.List;

@Dao
public interface TimetableDAO {

    @Insert
    Long insert(TimetableEntity timetableEntity);

    @Update
    void update(TimetableEntity timetableEntity);

    @Delete
    void delete(TimetableEntity timetableEntity);

    @Query("SELECT * FROM timetable_table")
    LiveData<List<TimetableEntity>> getAllTimetables();

    @Query("DELETE FROM timetable_table")
    void deleteAllTimetables();

    @Query("SELECT * FROM timetable_table WHERE isMainTimetable = 1")
    LiveData<TimetableEntity> getMainTimetable();

    @Query("UPDATE timetable_table SET isMainTimetable = CASE WHEN id = :timetableId THEN 1 " +
            "WHEN id !=:timetableId THEN 0 END")
    void setIsMainTimetable(int timetableId);

    // Not sure if need this method
    @Query("UPDATE timetable_table SET isMainTimetable = 0")
    void setAllTimetablesToNotMain();
}
