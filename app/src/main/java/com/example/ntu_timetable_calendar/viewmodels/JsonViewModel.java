package com.example.ntu_timetable_calendar.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ntu_timetable_calendar.models.jsonmodels.Course;
import com.example.ntu_timetable_calendar.models.jsonmodels.Exam;
import com.example.ntu_timetable_calendar.databaseservices.jsonrepository.JsonRepository;

import java.util.List;

public class JsonViewModel extends AndroidViewModel {

    private JsonRepository jsonRepository;

    public JsonViewModel(@NonNull Application application) {
        super(application);
        jsonRepository = new JsonRepository(application);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public LiveData<List<Course>> getFilteredCourseData() {
        return jsonRepository.getFilteredCourseList();
    }

    public LiveData<List<Exam>> getFilteredExamData() {
        return jsonRepository.getFilteredExamList();
    }

    public LiveData<List<String>> getAllCourseCode() {
        return jsonRepository.getAllCourseCode();
    }

    public LiveData<List<Course>> getTimetablePlanningCourseList() {
        return jsonRepository.getTimetablePlanningCourseList();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void queryCourseData(String queryStr) {
        new QueryCourseDataAsyncTask(queryStr, jsonRepository).execute();
    }

    private static class QueryCourseDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private String queryStr;
        private JsonRepository jsonRepository;

        QueryCourseDataAsyncTask(String queryStr, JsonRepository jsonRepository) {
            this.queryStr = queryStr;
            this.jsonRepository = jsonRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonRepository.queryCourseData(queryStr);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void queryExamData(List<String> queryStrList) {
        new QueryExamDataAsyncTask(queryStrList, jsonRepository).execute();
    }

    private static class QueryExamDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private List<String> queryStrList;
        private JsonRepository jsonRepository;

        QueryExamDataAsyncTask(List<String> queryStrList, JsonRepository jsonRepository) {
            this.queryStrList = queryStrList;
            this.jsonRepository = jsonRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonRepository.queryExamData(queryStrList);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void queryAllCourseCode() {
        new QueryAllCourseCodeAsyncTask(jsonRepository).execute();
    }

    private static class QueryAllCourseCodeAsyncTask extends AsyncTask<Void, Void, Void> {


        private JsonRepository jsonRepository;

        QueryAllCourseCodeAsyncTask(JsonRepository jsonRepository) {
            this.jsonRepository = jsonRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonRepository.queryAllCourseCode();
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void queryPlanningTimetableCourses(List<String> listOfQueries) {
        new QueryPlanningTimetableCoursesAsyncTaks(jsonRepository, listOfQueries).execute();
    }

    private static class QueryPlanningTimetableCoursesAsyncTaks extends AsyncTask<Void, Void, Void> {

        private JsonRepository jsonRepository;
        private List<String> listOfCourseCodes;

        QueryPlanningTimetableCoursesAsyncTaks(JsonRepository jsonRepository, List<String> listOfCourseCodes) {
            this.jsonRepository = jsonRepository;
            this.listOfCourseCodes = listOfCourseCodes;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            jsonRepository.queryPlanningTimetableCourses(listOfCourseCodes);
            return null;
        }
    }

}
