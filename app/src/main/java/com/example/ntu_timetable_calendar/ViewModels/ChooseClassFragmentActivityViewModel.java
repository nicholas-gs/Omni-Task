package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Calendar;

/**
 * Used by the ChooseClassFragment to manage it's UI during orientation change
 */
public class ChooseClassFragmentActivityViewModel extends AndroidViewModel {

    // The first day visible of the WeekView widget in the ChooseClassFragment Fragment
    private Calendar firstDayVisible;
    // The number of visible days selected by the user for the WeekView widget
    private Integer noOfVisibleDays;

    // Variables to save
    private Integer chosenClassId;
    private Calendar deadlineCalendar;
    private String chosenClassTitle, chosenClassTiming;

    public ChooseClassFragmentActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // Setters

    public void setFirstDayVisible(Calendar firstDayVisible) {
        this.firstDayVisible = firstDayVisible;
    }

    public void setNoOfVisibleDays(Integer noOfVisibleDays) {
        this.noOfVisibleDays = noOfVisibleDays;
    }

    public void setChosenClassId(Integer chosenClassId) {
        this.chosenClassId = chosenClassId;
    }

    public void setDeadlineCalendar(Calendar deadlineCalendar) {
        this.deadlineCalendar = deadlineCalendar;
    }

    public void setChosenClassTitle(String chosenClassTitle) {
        this.chosenClassTitle = chosenClassTitle;
    }

    public void setChosenClassTiming(String chosenClassTiming) {
        this.chosenClassTiming = chosenClassTiming;
    }

    // Getters

    public Calendar getFirstDayVisible() {
        return firstDayVisible;
    }

    public Integer getNoOfVisibleDays() {
        return noOfVisibleDays;
    }

    public Integer getChosenClassId() {
        return chosenClassId;
    }

    public Calendar getDeadlineCalendar() {
        return deadlineCalendar;
    }

    public String getChosenClassTitle() {
        return chosenClassTitle;
    }

    public String getChosenClassTiming() {
        return chosenClassTiming;
    }
}