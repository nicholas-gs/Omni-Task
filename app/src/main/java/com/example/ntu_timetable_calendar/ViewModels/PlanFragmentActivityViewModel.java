package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.JsonModels.Course;
import com.example.ntu_timetable_calendar.EventModel.Event;
import com.example.ntu_timetable_calendar.Converters.CourseToEventConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    // List of timetable events for display by the WeekView Widget
    private MutableLiveData<List<Event>> timetableEvents = new MutableLiveData<>();

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

    public LiveData<List<Event>> getTimetableEvents() {
        return timetableEvents;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Converts the list of courses and with it's selected indexes into Events POJO that can be displayed
     * by the WeekView Widget.
     */
    public void convertCoursesToEvents() {

        Map<String, String> tempIndexesSel = new HashMap<>();

        // DO A LITTLE HOUSE KEEPING AND REMOVE INDEXES FOR COURSES THAT ARE NO LONGER NEEDED!!
        if (queriedCourseList.size() != 0 && indexesSel.size() != 0) {
            for (String key : indexesSel.keySet()) {
                for (Course c : queriedCourseList) {
                    if (key.equals(c.getCourseCode().trim()))
                        tempIndexesSel.put(key, Objects.requireNonNull(indexesSel.get(key)));
                }
            }
            this.indexesSel.clear();
            this.indexesSel.putAll(tempIndexesSel);
        }

        new ConvertCoursesToEventsAsyncTask(this.queriedCourseList, indexesSel, this.timetableEvents).execute();
    }

    private static class ConvertCoursesToEventsAsyncTask extends AsyncTask<Void, Void, Void> {

        private List<Course> courseList;
        private Map<String, String> indexesSel;
        private MutableLiveData<List<Event>> eventList;

        ConvertCoursesToEventsAsyncTask(List<Course> courseList, Map<String, String> indexesSel,
                                        MutableLiveData<List<Event>> eventList) {
            this.courseList = courseList;
            this.indexesSel = indexesSel;
            this.eventList = eventList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventList.postValue(CourseToEventConverter.convertCourseToEvent(courseList, indexesSel));

            return null;
        }
    }
}
