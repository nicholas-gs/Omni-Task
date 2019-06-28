package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * This viewmodel is scoped to the activity's lifecycle and acts as a means of sharing data between fragments /
 * storing data for fragment reinitialisation.
 */
public class ActivityViewModel extends AndroidViewModel {

    private String searchQuery;

    public ActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

}
