package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.JsonModels.Course;

/**
 * This viewmodel is scoped to the activity's lifecycle and acts as a means of
 * storing data for search fragment's reinitialisation.
 */
public class SearchFragmentActivityViewModel extends AndroidViewModel {

    // Search Fragment search widget's query is stored here whenever the widget edittext changes
    private String searchQuery;

    // Pass the course object that the user clicked in SearchFragment's recyclerview to the CourseDetailFragment.
    private MutableLiveData<Course> courseToDetail = new MutableLiveData<>();

    public SearchFragmentActivityViewModel(@NonNull Application application) {
        super(application);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<Course> getCourseToDetail() {
        return courseToDetail;
    }

    public void setCourseToDetail(Course courseToDetail) {
        this.courseToDetail.postValue(courseToDetail);
    }

}
