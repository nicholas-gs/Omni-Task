package com.example.ntu_timetable_calendar.databaseservices.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ntu_timetable_calendar.databaseservices.dao.AlarmDAO;
import com.example.ntu_timetable_calendar.databaseservices.dao.CourseDAO;
import com.example.ntu_timetable_calendar.databaseservices.dao.CourseEventDAO;
import com.example.ntu_timetable_calendar.databaseservices.dao.ExamDAO;
import com.example.ntu_timetable_calendar.databaseservices.dao.ExamEventDAO;
import com.example.ntu_timetable_calendar.databaseservices.dao.ProjectDAO;
import com.example.ntu_timetable_calendar.databaseservices.dao.TaskDAO;
import com.example.ntu_timetable_calendar.databaseservices.dao.TimetableDAO;
import com.example.ntu_timetable_calendar.models.entities.AlarmEntity;
import com.example.ntu_timetable_calendar.models.entities.CourseEntity;
import com.example.ntu_timetable_calendar.models.entities.CourseEventEntity;
import com.example.ntu_timetable_calendar.models.entities.ExamEntity;
import com.example.ntu_timetable_calendar.models.entities.ExamEventEntity;
import com.example.ntu_timetable_calendar.models.entities.ProjectEntity;
import com.example.ntu_timetable_calendar.models.entities.TaskEntity;
import com.example.ntu_timetable_calendar.models.entities.TimetableEntity;
import com.example.ntu_timetable_calendar.converters.typeconverters.AlarmTimingChosenConverter;
import com.example.ntu_timetable_calendar.converters.typeconverters.AlarmTypeConverter;

@Database(entities = {TimetableEntity.class, CourseEntity.class, ExamEntity.class, CourseEventEntity.class,
        ExamEventEntity.class, TaskEntity.class, AlarmEntity.class, ProjectEntity.class},
        version = 1, exportSchema = false)
@TypeConverters(value = {AlarmTypeConverter.class, AlarmTimingChosenConverter.class})
public abstract class SQLDatabase extends RoomDatabase {

    private static SQLDatabase instance;

    public abstract TimetableDAO timetableDAO();

    public abstract CourseDAO courseDAO();

    public abstract ExamDAO examDAO();

    public abstract CourseEventDAO courseEventDAO();

    public abstract ExamEventDAO examEventDAO();

    public abstract TaskDAO taskDAO();

    public abstract AlarmDAO alarmDAO();

    public abstract ProjectDAO projectDAO();

    public static synchronized SQLDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SQLDatabase.class, "SQL_Database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}