package com.example.ntu_timetable_calendar.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "project_table")
public class ProjectEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private int priority;
    private int numberOfTasks;
    private int numberOfUncompletedTasks;

    public ProjectEntity(String name, String description, int priority, int numberOfTasks, int numberOfUncompletedTasks) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.numberOfTasks = numberOfTasks;
        this.numberOfUncompletedTasks = numberOfUncompletedTasks;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setNumberOfTasks(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
    }

    public void setNumberOfUncompletedTasks(int numberOfUncompletedTasks) {
        this.numberOfUncompletedTasks = numberOfUncompletedTasks;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public int getNumberOfTasks() {
        return numberOfTasks;
    }

    public int getNumberOfUncompletedTasks() {
        return numberOfUncompletedTasks;
    }
}