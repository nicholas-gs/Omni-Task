package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.Entity.TaskEntity;

import java.util.Calendar;

/**
 * ViewModel used to re-instantiate the fragment's variables
 * Used by AddNewTaskFragment & TaskDetailFragment
 */
public class TasksFragmentViewModel extends AndroidViewModel {

    public TasksFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    // In TaskDetailFragment, we store the original unedited task here!
    private TaskEntity taskEntity;

    private MutableLiveData<Integer> chosenClassId = new MutableLiveData<>();
    private Calendar deadLineCalendar;
    private boolean[] alarmTimingChosen;
    private int priorityChosen;

    // Setters

    public void setTaskEntity(TaskEntity taskEntity) {
        this.taskEntity = taskEntity;
    }

    public void setChosenClassId(int id) {
        this.chosenClassId.postValue(id);
    }

    public void setDeadLineCalendar(Calendar deadLineCalendar) {
        this.deadLineCalendar = deadLineCalendar;
    }

    public void setAlarmTimingChosen(boolean[] alarmTimingChosen) {
        this.alarmTimingChosen = alarmTimingChosen;
    }

    public void setPriorityChosen(int priorityChosen) {
        this.priorityChosen = priorityChosen;
    }

    // Getters

    public TaskEntity getTaskEntity() {
        return taskEntity;
    }

    public LiveData<Integer> getChosenClassId() {
        return chosenClassId;
    }

    public Calendar getDeadLineCalendar() {
        return deadLineCalendar;
    }

    public boolean[] getAlarmTimingChosen() {
        return alarmTimingChosen;
    }

    public int getPriorityChosen() {
        return priorityChosen;
    }
}