package com.example.ntu_timetable_calendar.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ntu_timetable_calendar.DAO.CourseDAO;
import com.example.ntu_timetable_calendar.DAO.TimetableDAO;
import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;

@Database(entities = {TimetableEntity.class, CourseEntity.class}, version = 1, exportSchema = false)
public abstract class SQLDatabase extends RoomDatabase {

    private static SQLDatabase instance;

    public abstract TimetableDAO timetableDAO();

    public abstract CourseDAO courseDAO();

    public static synchronized SQLDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SQLDatabase.class, "SQL_Database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }


}
