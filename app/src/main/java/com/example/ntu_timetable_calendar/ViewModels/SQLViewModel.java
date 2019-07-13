package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.SQLRepository.SQLRepository;

import java.util.List;

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

    public LiveData<List<TimetableEntity>> getAllTimetables() {
        return sqlRepository.getAllTimetables();
    }

    public LiveData<TimetableEntity> getMainTimetable() {
        return sqlRepository.getMainTimetable();
    }

    public void setIsMainTimetable(int timetableId) {
        sqlRepository.setIsMainTimetable(timetableId);
    }

    public void setAllTimetablesToNotMain() {
        sqlRepository.setAllTimetablesToNotMain();
    }

}
