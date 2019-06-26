package com.example.ntu_timetable_calendar.CourseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Course Model that does not have the course code
 */
public class Course {

    private String courseCode;
    private String name;
    private String au;
    @SerializedName("index")
    private List<Index> indexes;

    public Course(String courseCode ,String name, String au, List<Index> indexes) {
        this.courseCode = courseCode;
        this.name = name;
        this.au = au;
        this.indexes = indexes;
    }

    // Getters

    public String getCourseCode(){
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public String getAu() {
        return au;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    // Setter

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
