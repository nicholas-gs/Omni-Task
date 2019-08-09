package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.Entity.TaskEntity;

import java.util.Calendar;

/**
 * 1) ViewModel used to re-instantiate the fragment's variables
 * 2) Also used to share variables between ChooseClassFragment & AddNewTaskFragment/TaskDetailFragment
 * Used by AddNewTaskFragment & TaskDetailFragment
 */
public class TasksFragmentViewModel extends AndroidViewModel {

    public TasksFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    // In TaskDetailFragment, we store the original unedited task here!
    private TaskEntity taskEntity;

    // The ID of the chosen class is shared between the ChooseClassFragment & AddNewTaskFragment/TaskDetailFragment, with default value of -1
    private MutableLiveData<Integer> chosenClassId = new MutableLiveData<>(-1);
    // The title of the chosen class is shared between the ChooseClassFragment & AddNewTaskFragment/TaskDetailFragment
    private String chosenClassTitle;
    // The time of the chosen class is shared between the ChooseClassFragment & AddNewTaskFragment/TaskDetailFragment
    private String chosenClassTiming;

    private Calendar deadLineCalendar;
    private boolean[] alarmTimingChosen;
    private int priorityChosen;
    private boolean isDone;

    // Setters

    public void setTaskEntity(TaskEntity taskEntity) {
        this.taskEntity = taskEntity;
    }

    public void setChosenClassId(int id) {
        this.chosenClassId.postValue(id);
    }

    public void setChosenClassTitle(String chosenClassTitle) {
        this.chosenClassTitle = chosenClassTitle;
    }

    public void setChosenClassTiming(String chosenClassTiming) {
        this.chosenClassTiming = chosenClassTiming;
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

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    // Getters

    public TaskEntity getTaskEntity() {
        return taskEntity;
    }

    public LiveData<Integer> getChosenClassId() {
        return chosenClassId;
    }

    public String getChosenClassTitle() {
        return chosenClassTitle;
    }

    public String getChosenClassTiming() {
        return chosenClassTiming;
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

    public boolean getIsDone() {
        return isDone;
    }
}