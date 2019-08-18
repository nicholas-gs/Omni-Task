package com.example.ntu_timetable_calendar.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Calendar;

public class HomeFragmentActivityViewModel extends AndroidViewModel {

    // The first day visible of the WeekView widget in the Home Fragment
    private Calendar firstDayVisible;
    private Integer noOfVisibleDays;

    public HomeFragmentActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public Calendar getFirstDayVisible() {
        return firstDayVisible;
    }

    public void setFirstDayVisible(Calendar firstDayVisible) {
        this.firstDayVisible = firstDayVisible;
    }

    public Integer getNoOfVisibleDays() {
        return noOfVisibleDays;
    }

    public void setNoOfVisibleDays(Integer noOfVisibleDays) {
        this.noOfVisibleDays = noOfVisibleDays;
    }
}
