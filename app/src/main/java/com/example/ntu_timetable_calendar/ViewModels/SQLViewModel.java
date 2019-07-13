package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.SQLRepository.SQLRepository;

public class SQLViewModel extends AndroidViewModel implements SQLRepository.InsertTimetableCompletedListener {

    private SQLRepository sqlRepository;

    public SQLViewModel(@NonNull Application application) {
        super(application);
        this.sqlRepository = new SQLRepository(application);
        this.sqlRepository.setInsertTimetableCompletedListener(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Callback method returning the auto-generated timetable id when a new timetable is inserted by timetableDAO
     */
    public interface InsertTimetableCompletedListener {
        void onInsertCallback(Long timetableId);
    }

    private InsertTimetableCompletedListener mListener;

    public void setmListener(InsertTimetableCompletedListener insertTimetableCompletedListener) {
        this.mListener = insertTimetableCompletedListener;
    }

    /**
     * Callback for the SQLRepository's insert timetable method
     *
     * @param timetableId Id of the newly inserted timetable
     */
    @Override
    public void onInsertCallback(Long timetableId) {
        if (mListener != null) {
            mListener.onInsertCallback(timetableId);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void insertTimetable(TimetableEntity timetableEntity) {
        sqlRepository.insertTimetable(timetableEntity);
    }

    public void updateTimetable(TimetableEntity timetableEntity) {
        sqlRepository.updateTimetable(timetableEntity);
    }

    public void deleteTimetable(TimetableEntity timetableEntity) {
        sqlRepository.deleteTimetable(timetableEntity);
    }

    public void deleteAllTimetables() {
        sqlRepository.deleteAllTimetables();
    }

    public void getAllTimetables() {
        sqlRepository.getAllTimetables();
    }

    public void getMainTimetable() {
        sqlRepository.getMainTimetable();
    }

    public void setIsMainTimetable(int timetableId) {
        sqlRepository.setIsMainTimetable(timetableId);
    }

    public void setAllTimetablesToNotMain(){
        sqlRepository.setAllTimetablesToNotMain();
    }

}
