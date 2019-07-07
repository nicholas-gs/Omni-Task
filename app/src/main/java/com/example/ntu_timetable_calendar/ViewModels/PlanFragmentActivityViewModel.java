package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.ntu_timetable_calendar.CourseModels.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel tied to the life cycle of the activity -- used to restore the PLAN FRAGMENT
 */
public class PlanFragmentActivityViewModel extends AndroidViewModel {

    // A HashMap that stores the index selection for each course that the user has queried.
    private Map<String, String> indexesSel = new HashMap<>();

    // We store the list of courses sent by the JsonViewModel after sending the query
    private List<Course> queriedCourseList = new ArrayList<>();

    // Store the string inside the "Enter module" autocompletetextview
    private String enterModuleQuery = "";

    public PlanFragmentActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // Setters

    public void setIndexesSel(Map<String, String> indexesSel) {
        this.indexesSel = indexesSel;
    }

    public void setQueriedCourseList(List<Course> queriedCourseList) {
        this.queriedCourseList = queriedCourseList;
    }

    public void setEnterModuleQuery(String enterModuleQuery) {
        this.enterModuleQuery = enterModuleQuery;
    }

    // Getters

    public Map<String, String> getIndexesSel() {
        return indexesSel;
    }

    public List<Course> getQueriedCourseList() {
        return queriedCourseList;
    }

    public String getEnterModuleQuery() {
        return enterModuleQuery;
    }
}
