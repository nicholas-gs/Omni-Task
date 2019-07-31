package com.example.ntu_timetable_calendar.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ntu_timetable_calendar.DAO.CourseDAO;
import com.example.ntu_timetable_calendar.DAO.CourseEventDAO;
import com.example.ntu_timetable_calendar.DAO.ExamDAO;
import com.example.ntu_timetable_calendar.DAO.ExamEventDAO;
import com.example.ntu_timetable_calendar.DAO.TaskDAO;
import com.example.ntu_timetable_calendar.DAO.TimetableDAO;
import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;
import com.example.ntu_timetable_calendar.Entity.ExamEntity;
import com.example.ntu_timetable_calendar.Entity.ExamEventEntity;
import com.example.ntu_timetable_calendar.Entity.TaskEntity;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.TypeConverter.AlarmTypeConverter;

@Database(entities = {TimetableEntity.class, CourseEntity.class, ExamEntity.class, CourseEventEntity.class, ExamEventEntity.class, TaskEntity.class},
        version = 1, exportSchema = false)
@TypeConverters(value = {AlarmTypeConverter.class})
public abstract class SQLDatabase extends RoomDatabase {

    private static SQLDatabase instance;

    public abstract TimetableDAO timetableDAO();

    public abstract CourseDAO courseDAO();

    public abstract ExamDAO examDAO();

    public abstract CourseEventDAO courseEventDAO();

    public abstract ExamEventDAO examEventDAO();

    public abstract TaskDAO taskDAO();

    public static synchronized SQLDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SQLDatabase.class, "SQL_Database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}