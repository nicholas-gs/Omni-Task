package com.example.ntu_timetable_calendar.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ntu_timetable_calendar.Entity.ProjectEntity;

import java.util.List;

@Dao
public interface ProjectDAO {

    @Insert
    void insert(ProjectEntity projectEntity);

    @Update
    void update(ProjectEntity projectEntity);

    @Delete
    void delete(ProjectEntity projectEntity);

    @Query("SELECT * FROM project_table")
    LiveData<List<ProjectEntity>> getAllProjects();

}