package com.example.ntu_timetable_calendar.DAO;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.ExamModels.Exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is instantiated in JsonDatabase.java and acts as the API for access the json database contents.
 * All business logic, such as filtering results are defined here!
 */
public class JsonDAO {

    private List<Course> allCourses;
    private List<Exam> allExams;

    public JsonDAO(List<Course> allCourses, List<Exam> allExams) {
        this.allCourses = allCourses;
        this.allExams = allExams;
    }

    public List<Course> getAllCourses() {
        return allCourses;
    }

    public List<Exam> getAllExams() {
        return allExams;
    }

    /**
     * Takes in a list of courses (String) you want to select, and then searches through the map of all courses,
     * and then returns the selected courses.
     * If a course cannot be found, null is assigned to the key in the map.
     *
     * @param courseSelection
     * @return
     */
    public Map<String, Course> getSelectedCourses(@NonNull List<String> courseSelection) {
        // TODO
        return null;
    }

    /**
     * Takes in a list of exams (String) you want to select, and then searches through the map of all exams,
     * and then returns the selected exams.
     * If a exam cannot be found, null is assigned to the key in the map.
     *
     * @param examSelection
     * @return
     */
    public Map<String, Exam> getSelectedExams(@NonNull List<String> examSelection) {
        // TODO
        return null;
    }

}
