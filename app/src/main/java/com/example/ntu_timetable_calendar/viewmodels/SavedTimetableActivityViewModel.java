package com.example.ntu_timetable_calendar.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.ntu_timetable_calendar.models.entities.TimetableEntity;

public class SavedTimetableActivityViewModel extends AndroidViewModel {

    // TimetableEntity that is passed from the SavedTimetableFragment to the TimetableDetailFragment
    private TimetableEntity timetableEntity;

    public SavedTimetableActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // Setters

    public void setTimetableEntity(TimetableEntity timetableEntity) {
        this.timetableEntity = timetableEntity;
    }

    // Getters

    public TimetableEntity getTimetableEntity() {
        return timetableEntity;
    }

}
