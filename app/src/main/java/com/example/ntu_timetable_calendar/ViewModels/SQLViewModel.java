package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;
import com.example.ntu_timetable_calendar.Entity.ExamEntity;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.JsonModels.Course;
import com.example.ntu_timetable_calendar.JsonModels.Exam;
import com.example.ntu_timetable_calendar.SQLRepository.SQLRepository;

import java.util.List;
import java.util.Map;

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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<CourseEntity>> getAllCourses() {
        return sqlRepository.getAllCourses();
    }

    public LiveData<List<CourseEntity>> getTimetableCourses(int timetableId) {
        return sqlRepository.getTimetableCourses(timetableId);
    }

    public void insertCourses(int timetableId, List<Course> courseList, Map<String, String> indexSel) {
        sqlRepository.insertCourses(timetableId, courseList, indexSel);
    }

    public void updateCourse(CourseEntity courseEntity) {
        sqlRepository.updateCourse(courseEntity);
    }

    public void deleteCourse(CourseEntity courseEntity) {
        sqlRepository.deleteCourse(courseEntity);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<ExamEntity>> getAllExams() {
        return sqlRepository.getAllExams();
    }

    public LiveData<List<ExamEntity>> getTimetableExams(int timetableId) {
        return sqlRepository.getTimetableExams(timetableId);
    }

    public void insertExams(List<Exam> examList, int timetableId) {
        sqlRepository.insertExams(examList, timetableId);
    }

    public void updateExam(ExamEntity examEntity) {
        sqlRepository.updateExam(examEntity);
    }

    public void deleteExam(ExamEntity examEntity) {
        sqlRepository.deleteExam(examEntity);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<CourseEventEntity>> getAllCourseEvents() {
        return sqlRepository.getAllCourseEvents();
    }

    public LiveData<List<CourseEventEntity>> getTimetableCourseEvents(int timetableId) {
        return sqlRepository.getTimetableCourseEvents(timetableId);
    }

    public void insertCourseEvents(List<Course> courseList, Map<String, String> indexSel, int timetableId, int startMonth, int startDate) {
        sqlRepository.insertCourseEvents(courseList, indexSel, timetableId, startMonth, startDate);
    }

    public void updateCourseEvent(CourseEventEntity courseEventEntity) {
        sqlRepository.updateCourseEvent(courseEventEntity);
    }

    public void deleteCourseEvent(CourseEventEntity courseEventEntity) {
        sqlRepository.deleteCourseEvent(courseEventEntity);
    }

}
